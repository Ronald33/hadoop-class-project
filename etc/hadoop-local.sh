#!/bin/sh
export HADOOP_CLASSPATH="/Users/micinski/projects/hadoop-class-project/lib/activation-1.1.jar:/Users/micinski/projects/hadoop-class-project/lib/ant-1.6.5.jar:/Users/micinski/projects/hadoop-class-project/lib/aopalliance-1.0.jar:/Users/micinski/projects/hadoop-class-project/lib/asm-3.2.jar:/Users/micinski/projects/hadoop-class-project/lib/avro-1.3.2.jar:/Users/micinski/projects/hadoop-class-project/lib/avro-1.7.3.jar:/Users/micinski/projects/hadoop-class-project/lib/bliki-core-3.0.16.jar:/Users/micinski/projects/hadoop-class-project/lib/cglib-2.2.1-v20090111.jar:/Users/micinski/projects/hadoop-class-project/lib/cloud9-1.4.13.jar:/Users/micinski/projects/hadoop-class-project/lib/collections-generic-4.01.jar:/Users/micinski/projects/hadoop-class-project/lib/colt-1.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-beanutils-1.7.0.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-beanutils-core-1.8.0.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-cli-1.2.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-codec-1.4.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-collections-3.2.1.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-compress-1.0.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-configuration-1.6.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-daemon-1.0.3.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-digester-1.8.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-el-1.0.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-httpclient-3.1.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-io-2.1.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-lang-2.6.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-logging-1.1.1.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-math-2.1.jar:/Users/micinski/projects/hadoop-class-project/lib/commons-net-3.1.jar:/Users/micinski/projects/hadoop-class-project/lib/concurrent-1.3.4.jar:/Users/micinski/projects/hadoop-class-project/lib/core-3.1.1.jar:/Users/micinski/projects/hadoop-class-project/lib/gson-2.2.2.jar:/Users/micinski/projects/hadoop-class-project/lib/guava-13.0.1.jar:/Users/micinski/projects/hadoop-class-project/lib/guice-3.0.jar:/Users/micinski/projects/hadoop-class-project/lib/guice-servlet-3.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-annotations-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-auth-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-common-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-hdfs-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-mapreduce-client-core-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-streaming-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-yarn-api-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-yarn-common-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-yarn-server-common-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-yarn-server-nodemanager-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-yarn-server-resourcemanager-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hadoop-yarn-server-web-proxy-2.0.0-cdh4.2.0.jar:/Users/micinski/projects/hadoop-class-project/lib/hamcrest-core-1.3.jar:/Users/micinski/projects/hadoop-class-project/lib/hsqldb-1.8.0.10.jar:/Users/micinski/projects/hadoop-class-project/lib/htmlparser-1.6.jar:/Users/micinski/projects/hadoop-class-project/lib/jackson-core-asl-1.8.8.jar:/Users/micinski/projects/hadoop-class-project/lib/jackson-jaxrs-1.8.8.jar:/Users/micinski/projects/hadoop-class-project/lib/jackson-mapper-asl-1.8.8.jar:/Users/micinski/projects/hadoop-class-project/lib/jackson-xc-1.8.8.jar:/Users/micinski/projects/hadoop-class-project/lib/jasper-compiler-5.5.12.jar:/Users/micinski/projects/hadoop-class-project/lib/jasper-runtime-5.5.23.jar:/Users/micinski/projects/hadoop-class-project/lib/javax.inject-1.jar:/Users/micinski/projects/hadoop-class-project/lib/jaxb-api-2.2.2.jar:/Users/micinski/projects/hadoop-class-project/lib/jaxb-impl-2.2.3-1.jar:/Users/micinski/projects/hadoop-class-project/lib/jersey-core-1.8.jar:/Users/micinski/projects/hadoop-class-project/lib/jersey-guice-1.8.jar:/Users/micinski/projects/hadoop-class-project/lib/jersey-json-1.8.jar:/Users/micinski/projects/hadoop-class-project/lib/jersey-server-1.8.jar:/Users/micinski/projects/hadoop-class-project/lib/jersey-test-framework-grizzly2-1.8.jar:/Users/micinski/projects/hadoop-class-project/lib/jets3t-0.7.1.jar:/Users/micinski/projects/hadoop-class-project/lib/jettison-1.1.jar:/Users/micinski/projects/hadoop-class-project/lib/jetty-6.1.26.jar:/Users/micinski/projects/hadoop-class-project/lib/jetty-util-6.1.26.jar:/Users/micinski/projects/hadoop-class-project/lib/jsch-0.1.42.jar:/Users/micinski/projects/hadoop-class-project/lib/jsp-2.1-6.1.14.jar:/Users/micinski/projects/hadoop-class-project/lib/jsp-api-2.1-6.1.14.jar:/Users/micinski/projects/hadoop-class-project/lib/jsp-api-2.1.jar:/Users/micinski/projects/hadoop-class-project/lib/jung-algorithms-2.0.1.jar:/Users/micinski/projects/hadoop-class-project/lib/jung-api-2.0.1.jar:/Users/micinski/projects/hadoop-class-project/lib/jung-graph-impl-2.0.1.jar:/Users/micinski/projects/hadoop-class-project/lib/junit-4.11.jar:/Users/micinski/projects/hadoop-class-project/lib/jwnl-1.3.3.jar:/Users/micinski/projects/hadoop-class-project/lib/kfs-0.3.jar:/Users/micinski/projects/hadoop-class-project/lib/log4j-1.2.16.jar:/Users/micinski/projects/hadoop-class-project/lib/maxent-3.0.0.jar:/Users/micinski/projects/hadoop-class-project/lib/mockito-all-1.8.5.jar:/Users/micinski/projects/hadoop-class-project/lib/mrunit-0.8.0-incubating.jar:/Users/micinski/projects/hadoop-class-project/lib/netty-3.2.4.Final.jar:/Users/micinski/projects/hadoop-class-project/lib/oro-2.0.8.jar:/Users/micinski/projects/hadoop-class-project/lib/paranamer-2.3.jar:/Users/micinski/projects/hadoop-class-project/lib/paranamer-ant-2.2.jar:/Users/micinski/projects/hadoop-class-project/lib/paranamer-generator-2.2.jar:/Users/micinski/projects/hadoop-class-project/lib/pcj-1.2.jar:/Users/micinski/projects/hadoop-class-project/lib/pig-0.10.0.jar:/Users/micinski/projects/hadoop-class-project/lib/protobuf-java-2.4.0a.jar:/Users/micinski/projects/hadoop-class-project/lib/qdox-1.10.1.jar:/Users/micinski/projects/hadoop-class-project/lib/servlet-api-2.5-20081211.jar:/Users/micinski/projects/hadoop-class-project/lib/servlet-api-2.5-6.1.14.jar:/Users/micinski/projects/hadoop-class-project/lib/servlet-api-2.5.jar:/Users/micinski/projects/hadoop-class-project/lib/slf4j-api-1.6.1.jar:/Users/micinski/projects/hadoop-class-project/lib/slf4j-log4j12-1.6.1.jar:/Users/micinski/projects/hadoop-class-project/lib/snappy-java-1.0.4.1.jar:/Users/micinski/projects/hadoop-class-project/lib/stax-api-1.0.1.jar:/Users/micinski/projects/hadoop-class-project/lib/tools-1.5.0.jar:/Users/micinski/projects/hadoop-class-project/lib/xmlenc-0.52.jar:/Users/micinski/projects/hadoop-class-project/dist/mapreduce-assignment-0.0.1.jar:$HADOOP_CLASSPATH"
hadoop jar /Users/micinski/projects/hadoop-class-project/dist/mapreduce-assignment-0.0.1.jar $1 -D mapreduce.framework.name=local -D mapreduce.jobtracker.address=local -D fs.default.name=file:/// -D mapreduce.cluster.local.dir=/tmp/mapred/local -D mapreduce.cluster.temp.dir=/tmp/mapred/temp -D mapreduce.jobtracker.staging.root.dir=/tmp/mapred/staging -D mapreduce.jobtracker.system.dir=/tmp/mapred/system $2 $3 $4 $5 $6 $7 $8 $9 ${10} ${11} ${12} ${13} ${14} ${15} ${16} ${17} ${18} ${19} ${20}
