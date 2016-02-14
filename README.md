# JobProxy

JobProxy is (should be) a proxy between a application that needs access to compute resources like starting a docker container or just starting a simple shell script and the various existing resource providing frameworks. 


## Motivation
While developing an application that needs access to compute resources from a running Mesos, we were searching for a Mesos framework that fullfills our constraints (which were very simple at this point). However we found that there are exists more than one framework that can do the job, but which is right/best one for our application ? A proxy as abstraction layer between our application and the frameworks seems to be very helpful. The decision which framework to be used is then indepened from writting the application ...


