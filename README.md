# JobProxy

[![](https://jitpack.io/v/jkrue/jobproxy.svg)](https://jitpack.io/#jkrue/jobproxy)

JobProxy is (should be) a proxy between an application that needs access to compute resources like starting a docker container or just starting a simple shell script and the various existing resource providing frameworks. 

## Motivation

While developing an application that needs access to compute resources from a running Mesos, we were searching for a Mesos framework that fullfills our constraints (which were very simple at this point). However we found that there are exists more than one framework that can do the job, but which is right/best one for our application ? A proxy as abstraction layer between our application and the frameworks seems to be very helpful. The decision which framework to be used is then indepened from writting the application ...

## Requirements:

* Java 8

## User Guide

#### Installation:

Download the latest jar with the following command:

~~~BASH
wget https://jitpack.io/com/github/jkrue/jobproxy/JobProxyServer/RELEASE/JobProxyServer-RELEASE.jar
~~~

where 
  
  * **RELEASE** is an identifier you can find on release [page](https://github.com/jkrue/jobproxy/releases) i.e: `0.1.0.alpha.2`.

#### Usage:

See  [JobProxyServerCLI](JobProxyServerCLI) for
usage example.


### List of implementing Frameworks:

* Mesos Chronos (https://mesos.github.io/chronos/) version 2.4.0
* DRMAA (https://www.drmaa.org/) using DRMAA V1.0 API
* JavaDocker (https://github.com/docker-java/docker-java)

## Developer Guide

### Release/Branch Workflow

For each release a new branch must be created.

E.g: 

~~~
beta.release.1
~~~

On a release, the list of implementing Frameworks must be extended.

Then the release branch will be merged into the master.

Each module has its own version number e.g.: https://github.com/jkrue/jobproxy/blob/master/JobProxyServer/pom.xml#L8
 
If the code is updated the corresponding module version number must be updated. 

### How to use jobProxy as a dependency?

Just go to [this site](https://jitpack.io/#jkrue/jobproxy) and follow the instructions.

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

You will find the jar inside the `JobProxy/JobProxyServerCLI/target` folder.

### REST

We are using Swagger for our REST API. Our swagger.yaml can be found inside our `doc` [directory](doc/REST) altogether with our
latest REST API [documentation](doc/REST/api.md).

#### How to extend our REST documentation?

We are generating markup out of our swagger yaml by using the [swagger2markup-cli](http://swagger2markup.github.io/swagger2markup/1.0.1-SNAPSHOT/#_command_line_interface) tool.
With the following command:

~~~BASH
java -jar swagger2markup-cli-1.0.0.jar convert -c config.properties -i swagger.yaml -f doc.md
~~~

where
   
   * doc.md is the output document
   
   * config.properties is the [configuration file](doc/REST/config.properties)

#### How to build your own jobproxy client?

Just use the swagger [code generator](https://github.com/swagger-api/swagger-codegen) with our swagger.yaml to produce jobproxy client code in your favorite language.

The following call produces an java client from swagger specification:

~~~BASH
swagger-codegen-cli.sh generate -i doc/REST/swagger.yaml -l java 
~~~

