# JobProxy

JobProxy is (should be) a proxy between an application that needs access to compute resources like starting a docker container or just starting a simple shell script and the various existing resource providing frameworks. 

## Motivation
While developing an application that needs access to compute resources from a running Mesos, we were searching for a Mesos framework that fullfills our constraints (which were very simple at this point). However we found that there are exists more than one framework that can do the job, but which is right/best one for our application ? A proxy as abstraction layer between our application and the frameworks seems to be very helpful. The decision which framework to be used is then indepened from writting the application ...

## Developer Guide

### How to build to build JobProxy?

1. Clone this project.

2. Compile this project with Maven.

~~~Bash
mvn clean compile
~~~

### How to package JobProxy?

Run the following command for packaging. 

~~~Bash
mvn package
~~~

You will find the jar inside the `JobProxy/JobProxyServer/target` folder.

### REST

We are using Swagger for our REST API. Our swagger.yaml can be found inside our `doc` [directory](doc/REST) altogether with our
latest REST API [documentation]().

#### How to extend our REST documentation?

We are generating markup out of our swagger yaml by using the [swagger2markup](https://swagger2markup.readme.io/docs) tool.

#### How to build your own jobproxy client?

Just use the swagger [code generator](https://github.com/swagger-api/swagger-codegen) with our swagger.yaml to produce jobproxy client code in your favorite language.


