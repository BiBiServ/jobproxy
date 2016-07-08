# JobProxyServerCLI

The JobProxyServerCLI is the commandlineinterface for an JobProxy Server.

## supported options

~~~BASH
>java -jar JobProxyServerCLI/target/JobProxyServerCLI-beta.1.release.jar 
usage: java -jar JobProxy.jar
 -d         start server in daemon mode
 -debug     run server in debug mode. Logs all http request/responses.
 -f <arg>   Framework/Plugin to be used by JobProxy.
 -h         Print general help or help for a specified framework together
            with '-f' option
 -l         List all available frameworks/plugins.
 -p <arg>   Configuration file (java properties style)
~~~

## run JobProxy as daemon

Running a Java application (like JobProxy is one) as service needs some additional 
shell magic to work. The following snippet is an example for running JobProxy on top 
of a DRMAA compatible compute grid as background process (daemon):


~~~
LOGFILE=jobproxy.log
PIDFILE=jobproxy.pid
PROPERTIES=/home/juser/jobproxy.properties
JAVA_OPTS="-Djava.library.path=/vol/codine-8.3/lib/sol-amd64"
JOBPROXY_OPTS="-f DRMAA -p $PROPERTIES -d"
nohup java $JAVA_OPTS -jar JobProxy-beta.1.release.jar $JOBPROXY_OPTS </dev/null > $LOGFILE 2>&1 &
echo $! > $PIDFILE
~~~