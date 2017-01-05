FROM greyrocksoftware/javastandalone7

# Set the locale
RUN locale-gen en_US.UTF-8
ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en' LC_ALL='en_US.UTF-8'

RUN apt-get update

RUN apt-get install git -y

ADD run_tests.sh /opt/

ENTRYPOINT ["/opt/run_tests.sh"]

