FROM greyrocksoftware/javastandalone

# Set the locale
RUN locale-gen en_US.UTF-8
ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en' LC_ALL='en_US.UTF-8'

RUN apt-get update && apt-get install -y \
  git


ADD run_tests.sh /opt/

ENTRYPOINT ["/opt/run_tests.sh"]

