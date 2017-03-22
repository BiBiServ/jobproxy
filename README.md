# JobProxy

[![](https://jitpack.io/v/BiBiServ/jobproxy.svg)](https://jitpack.io/#BiBiServ/jobproxy)

[![CircleCI](https://circleci.com/gh/BiBiServ/jobproxy/tree/development.svg?style=svg)](https://circleci.com/gh/BiBiServ/jobproxy/tree/development)

JobProxy is (should be) a proxy between an application that needs access to compute resources like starting a docker container or just starting a simple shell script and the various existing resource providing frameworks. 

## Motivation

While developing an application that needs access to compute resources from a running Mesos, we were searching for a Mesos framework that fullfills our constraints (which were very simple at this point). However we found that there are exists more than one framework that can do the job, but which is right/best one for our application ? A proxy as abstraction layer between our application and the frameworks seems to be very helpful. The decision which framework to be used is then indepened from writting the application ...

## Requirements:

* Java 8

## User Guide

#### Installation:

Download the latest jar with the following command:

~~~BASH
wget https://jitpack.io/com/github/BiBiServ/jobproxy/JobProxyServerCLI/RELEASE/JobProxyServerCLI-RELEASE.jar
~~~

where 
  
  * **RELEASE** is an identifier you can find on release [page](https://github.com/BiBiServ/jobproxy/releases) i.e: `0.1.0.alpha.2`.

#### Usage:

See  [JobProxyServerCLI](JobProxyServerCLI) for
usage example.

### List of implementing Frameworks:

* Mesos Chronos (https://mesos.github.io/chronos/) version 2.4.0
* DRMAA (https://www.drmaa.org/) using DRMAA V1.0 API
* JavaDocker (https://github.com/spotify/docker-client)
* Kubernetes (https://kubernetes.io)

## Developer Guide

### Development-Guidelines

https://github.com/BiBiServ/Development-Guidelines

### Release/Development Branch Workflow

There are two branches one is the **master** branch with latest working version of JobProxy and the other one is
the **development** branch for the next release.

On a release, the development branch must be merged into the master branch and the list of implementing Frameworks must be extended.

Each module has its own version number e.g.: https://github.com/jkrue/jobproxy/blob/master/JobProxyServer/pom.xml#L8
 
If the code is updated, the corresponding module version number must be updated. 

### How to use jobProxy as a dependency?

Just go to [this site](https://jitpack.io/#BiBiServ/jobproxy) and follow the instructions.
Please keep in mind that you can import single subproject artifacts like **JobProxyServer** and 
not the whole **JobProxy** parent artifact.

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

#### How to version the project?

We decided that all modules should have the same version as the parent module.
By using the below command in the project root you can update all child modules at once.

~~~BASH
mvn  versions:set -DnewVersion=<version>
~~~

where
  
 * version = **2.1.0.alpha.2**

## Vagrant test environments

Vagrant is a handy tool which helps to setup "quasi"-real test environment for testing JobProxy's Modules.
### Installing requirements for Vagrant environment
If you want to test JobProxy Modules you will need the following tools:

| Tool | Installation command for ubuntu |
| ------ | ------ |
| VirtualBox | ``` $ sudo apt-get install virtualbox virtualbox-dkms ``` |
| Vagrant | You can find package for Ubuntu/Debian  [here](https://www.vagrantup.com/downloads.html)|
| Vagrant Hostmanager | ``` $ vagrant plugin install vagrant-hostmanager ``` |
| GNU Parallel | ``` $ sudo apt-get install parallel ``` |

### Starting the environment
After all prerequisites are done you should be able to start the environment.
##### Important Note
**since Vagrant is not able to start multiple machines in parallel due to instable behavior of VirtualBox ([s. here ](https://www.vagrantup.com/docs/virtualbox/usage.html)), the start via ``` $ vagrant up ``` takes long time ans is therefore not used.**

To start the Vagrant environment navigate to the subfolder containing the Vagrant file and run

```sh
$ ./parallel_provision.sh
```
This script lets start all machines sequentally without configuration and performs configuration in parallel after all machines are boot up.

### Example: JobProxyChronos
```sh
$ cd JobProxyChronos/testing
$ ./parallel_provision.sh
```

After the script is done you will get 1 Master node running ZooKepper and Chronos and 3 slave Mesos nodes. You'll also be able to access Mesos framework at http://10.0.0.2:5050 and Chronos Web GUI at http://10.0.0.2:8081  
