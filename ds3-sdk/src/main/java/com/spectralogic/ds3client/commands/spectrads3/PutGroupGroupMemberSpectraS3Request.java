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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import java.util.UUID;
import com.google.common.net.UrlEscapers;

public class PutGroupGroupMemberSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String groupId;

    private final String memberGroupId;

    // Constructor
    
    
    public PutGroupGroupMemberSpectraS3Request(final UUID groupId, final UUID memberGroupId) {
        this.groupId = groupId.toString();
        this.memberGroupId = memberGroupId.toString();
        
        this.getQueryParams().put("group_id", groupId.toString());
        this.getQueryParams().put("member_group_id", memberGroupId.toString());
    }

    
    public PutGroupGroupMemberSpectraS3Request(final String groupId, final String memberGroupId) {
        this.groupId = groupId;
        this.memberGroupId = memberGroupId;
        
        this.getQueryParams().put("group_id", UrlEscapers.urlFragmentEscaper().escape(groupId).replace("+", "%2B"));
        this.getQueryParams().put("member_group_id", UrlEscapers.urlFragmentEscaper().escape(memberGroupId).replace("+", "%2B"));
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/group_member";
    }
    
    public String getGroupId() {
        return this.groupId;
    }


    public String getMemberGroupId() {
        return this.memberGroupId;
    }

}