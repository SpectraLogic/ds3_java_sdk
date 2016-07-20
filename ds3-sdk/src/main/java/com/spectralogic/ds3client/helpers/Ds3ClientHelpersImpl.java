/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client.helpers;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.helpers.options.ReadJobOptions;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.helpers.util.PartialObjectHelpers;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.RequestType;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Ds3ClientHelpersImpl extends Ds3ClientHelpers {

    private final static Logger LOG = LoggerFactory.getLogger(Ds3ClientHelpersImpl.class);

    private static final int DEFAULT_MAX_KEYS = 1000;
    private final Ds3Client client;
    private final int retryAfter;

    public Ds3ClientHelpersImpl(final Ds3Client client) {
        this(client, -1);
    }

    public Ds3ClientHelpersImpl(final Ds3Client client, final int retryAfter) {
        this.client = client;
        this.retryAfter = retryAfter;
    }

    @Override
    public Ds3ClientHelpers.Job startWriteJob(final String bucket, final Iterable<Ds3Object> objectsToWrite)
            throws SignatureException, IOException, XmlProcessingException {
        return innerStartWriteJob(bucket, objectsToWrite, WriteJobOptions.create());
    }

    @Override
    public Ds3ClientHelpers.Job startWriteJob(final String bucket,
                                                   final Iterable<Ds3Object> objectsToWrite,
                                                   final WriteJobOptions options)
            throws SignatureException, IOException, XmlProcessingException {
        if (options == null) {
            return innerStartWriteJob(bucket, objectsToWrite, WriteJobOptions.create());
        }
        return innerStartWriteJob(bucket, objectsToWrite, options);
    }

    private Ds3ClientHelpers.Job innerStartWriteJob(final String bucket,
                                                         final Iterable<Ds3Object> objectsToWrite,
                                                         final WriteJobOptions options)
            throws SignatureException, IOException, XmlProcessingException {
        final PutBulkJobSpectraS3Response prime = this.client.putBulkJobSpectraS3(
                new PutBulkJobSpectraS3Request(bucket, Lists.newArrayList(objectsToWrite))
                .withPriority(options.getPriority())
                .withMaxUploadSize(options.getMaxUploadSize())
                .withAggregating(options.isAggregating()));
        return new WriteJobImpl(this.client, prime.getResult(), this.retryAfter, options.getChecksumType());
    }

    @Override
    public Ds3ClientHelpers.Job startReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead)
            throws SignatureException, IOException, XmlProcessingException {
        return innerStartReadJob(bucket, objectsToRead, ReadJobOptions.create());
    }

    @Override
    public Ds3ClientHelpers.Job startReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead, final ReadJobOptions options)
            throws SignatureException, IOException, XmlProcessingException {
        if (options == null) {
            return innerStartReadJob(bucket, objectsToRead, ReadJobOptions.create());
        }
        return innerStartReadJob(bucket, objectsToRead, options);
    }

    private Ds3ClientHelpers.Job innerStartReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead, final ReadJobOptions options)
            throws SignatureException, IOException, XmlProcessingException {
        final List<Ds3Object> objects = Lists.newArrayList(objectsToRead);
        final GetBulkJobSpectraS3Response prime = this.client.getBulkJobSpectraS3(new GetBulkJobSpectraS3Request(bucket, objects)
                .withChunkClientProcessingOrderGuarantee(JobChunkClientProcessingOrderGuarantee.NONE)
                .withPriority(options.getPriority()).withName(options.getName()));

        final ImmutableMultimap<String, Range> partialRanges = PartialObjectHelpers.getPartialObjectsRanges(objects);

        return new ReadJobImpl(this.client, prime.getResult(), partialRanges, this.retryAfter);
    }

    @Override
    public Ds3ClientHelpers.Job startReadAllJob(final String bucket)
            throws SignatureException, IOException, XmlProcessingException {
        return innerStartReadAllJob(bucket, ReadJobOptions.create());
    }

    @Override
    public Ds3ClientHelpers.Job startReadAllJob(final String bucket, final ReadJobOptions options)
            throws SignatureException, IOException, XmlProcessingException {
        if (options == null) {
            return innerStartReadAllJob(bucket, ReadJobOptions.create());
        }
        return innerStartReadAllJob(bucket, options);
    }

    private Ds3ClientHelpers.Job innerStartReadAllJob(final String bucket, final ReadJobOptions options)
            throws SignatureException, IOException, XmlProcessingException {
        final Iterable<Contents> contentsList = this.listObjects(bucket);

        final Iterable<Ds3Object> ds3Objects = this.toDs3Iterable(contentsList, FolderNameFilter.filter());

        return this.startReadJob(bucket, ds3Objects, options);
    }

    @Override
    public Ds3ClientHelpers.Job recoverWriteJob(final UUID jobId) throws SignatureException, IOException, XmlProcessingException, JobRecoveryException {
        final ModifyJobSpectraS3Response jobResponse = this.client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(jobId));
        if (JobRequestType.PUT != jobResponse.getMasterObjectListResult().getRequestType()) {
            throw new JobRecoveryException(
                    RequestType.PUT.toString(),
                    jobResponse.getMasterObjectListResult().getRequestType().toString());
        }
        // TODO Need to allow the user to pass in the checksumming information again
        return new WriteJobImpl(
                this.client,
                jobResponse.getMasterObjectListResult(),
                this.retryAfter,
                ChecksumType.Type.NONE);
    }

    @Override
    //TODO add a partial object read recovery method.  That method will require the list of partial objects.
    public Ds3ClientHelpers.Job recoverReadJob(final UUID jobId) throws SignatureException, IOException, XmlProcessingException, JobRecoveryException {
        final ModifyJobSpectraS3Response jobResponse = this.client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(jobId));
        if (JobRequestType.GET != jobResponse.getMasterObjectListResult().getRequestType()){
            throw new JobRecoveryException(
                    RequestType.GET.toString(),
                    jobResponse.getMasterObjectListResult().getRequestType().toString() );
        }
        return new ReadJobImpl(
                this.client,
                jobResponse.getMasterObjectListResult(),
                ImmutableMultimap.<String, Range>of(),
                this.retryAfter);
    }

    @Override
    public void ensureBucketExists(final String bucket) throws IOException, SignatureException {
        final HeadBucketResponse response = this.client.headBucket(new HeadBucketRequest(bucket));
        if (response.getStatus() == HeadBucketResponse.Status.DOESNTEXIST) {
            try {
                this.client.putBucket(new PutBucketRequest(bucket));
            } catch (final FailedRequestException e) {
                if (e.getStatusCode() != 409) {
                    throw e;
                }
                LOG.warn("Creating " + bucket + " failed because it was created by another thread or process");
            }
        }
    }

    @Override
    public Iterable<Contents> listObjects(final String bucket) throws SignatureException, IOException {
        return this.listObjects(bucket, null);
    }

    @Override
    public Iterable<Contents> listObjects(final String bucket, final String keyPrefix) throws SignatureException, IOException {
        return this.listObjects(bucket, keyPrefix, null, Integer.MAX_VALUE);
    }

    @Override
    public Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker) throws SignatureException, IOException {
        return this.listObjects(bucket, keyPrefix, nextMarker, Integer.MAX_VALUE);
    }

    @Override
    public Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys) throws SignatureException, IOException {
        final List<Contents> objectApiBeans = new ArrayList<>();

        int remainingKeys = maxKeys;
        boolean isTruncated = false;
        String marker = nextMarker;
        if (nextMarker != null) isTruncated = true;

        do {
            final GetBucketRequest request = new GetBucketRequest(bucket);
            request.withMaxKeys(Math.min(remainingKeys, DEFAULT_MAX_KEYS));
            if (keyPrefix != null) {
                request.withPrefix(keyPrefix);
            }
            if (isTruncated) {
                request.withMarker(marker);
            }

            final GetBucketResponse response = this.client.getBucket(request);
            final ListBucketResult result = response.getListBucketResult();

            isTruncated = result.getTruncated();
            marker = result.getNextMarker();
            remainingKeys -= result.getObjects().size();

            for (final Contents objectApiBean : result.getObjects()) {
                objectApiBeans.add(objectApiBean);
            }
        } while (isTruncated && remainingKeys > 0);

        return objectApiBeans;
    }

    @Override
    public Iterable<Ds3Object> listObjectsForDirectory(final Path directory) throws IOException {
        final List<Ds3Object> objects = new ArrayList<>();
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                objects.add(new Ds3Object(directory.relativize(file).toString().replace("\\", "/"), Files.size(file)));
                return FileVisitResult.CONTINUE;
            }
        });
        return objects;
    }

    public Iterable<Ds3Object> addPrefixToDs3ObjectsList(final Iterable<Ds3Object> objectsList, final String prefix) {
        final List<Ds3Object> newObjectsList = new ArrayList<>();
        for (final Ds3Object object: objectsList) {
            final Ds3Object tmpObj = new Ds3Object( prefix + object.getName(), object.getSize());
            newObjectsList.add(tmpObj);
        }
        return newObjectsList;
    }

    public Iterable<Ds3Object> removePrefixFromDs3ObjectsList(final Iterable<Ds3Object> objectsList, final String prefix) {
        final List<Ds3Object> newObjectsList = new ArrayList<>();
        for (final Ds3Object object: objectsList) {
            newObjectsList.add(new Ds3Object(stripLeadingPath(object.getName(), prefix), object.getSize()));
        }
        return newObjectsList;
    }

    @Override
    public Iterable<Ds3Object> toDs3Iterable(final Iterable<Contents> objects) {
        return toDs3Iterable(objects, null);
    }

    @Override
    public Iterable<Ds3Object> toDs3Iterable(final Iterable<Contents> objects, final Predicate<Contents> filter) {
        return FluentIterable.from(objects).filter(new com.google.common.base.Predicate<Contents>() {
            @Override
            public boolean apply(@Nullable final Contents input) {
                return input != null;
            }
        }).filter(new com.google.common.base.Predicate<Contents>() {
            @Override
            public boolean apply(@Nullable final Contents input) {
                if (filter != null) {
                    return filter.test(input);
                } else {
                    return true; // do not filter anything if filter is null
                }
            }
        }).transform(new Function<Contents, Ds3Object>() {
            @Nullable
            @Override
            public Ds3Object apply(@Nullable final Contents input) {
                return new Ds3Object(input.getKey(), input.getSize());
            }
        });
    }
}
