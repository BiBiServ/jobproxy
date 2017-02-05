# JobProxy


<a name="overview"></a>
## Overview
JobProxy REST API for running task independent of the framework


### Version information
*Version* : 0.1.0.alpha.9


### URI scheme
*BasePath* : /v1  
*Schemes* : HTTP, HTTPS


### Produces

* `application/json`




<a name="paths"></a>
## Paths

<a name="jobproxy-delete-id-delete"></a>
### Delete a Task
```
DELETE /jobproxy/delete/{id}
```


#### Description
The delete endpoint accepts a task id and deletes the corresponding task.


#### Parameters

|Type|Name|Schema|
|---|---|---|
|**Path**|**id**  <br>*required*|string|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|Task is deleted|No Content|


#### Consumes

* `text/plain`


<a name="jobproxy-state-get"></a>
### Tasks states
```
GET /jobproxy/state
```


#### Description
Returns task states.


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Task IDs returned|No Content|


<a name="jobproxy-state-id-get"></a>
### Tasks state
```
GET /jobproxy/state/{id}
```


#### Description
The states endpoint accepts a task id.


#### Parameters

|Type|Name|Schema|
|---|---|---|
|**Path**|**id**  <br>*required*|string|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Accepted Task ID|No Content|


#### Consumes

* `application/json`
* `application/xml`


<a name="jobproxy-submit-post"></a>
### Tasks to be run
```
POST /jobproxy/submit
```


#### Description
The submit endpoint accepts a task definition which is used to run
on a fraemwork.


#### Parameters

|Type|Name|Schema|
|---|---|---|
|**Body**|**Task**  <br>*required*|[Task](#task)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Accepted Task|No Content|


#### Consumes

* `application/json`
* `application/xml`




<a name="definitions"></a>
## Definitions

<a name="container"></a>
### Container
Optional Docker container.


|Name|Description|Schema|
|---|---|---|
|**image**  <br>*required*|Docker image specification|string|
|**ports**  <br>*optional*||[Ports](#ports)|
|**volumes**  <br>*optional*||[Mounts](#mounts)|


<a name="mount"></a>
### Mount
Optional Volumes for Docker containers.


|Name|Description|Schema|
|---|---|---|
|**container**  <br>*required*|Path to a file or directory inside the container|string|
|**host**  <br>*required*|Path to a file or directory on the host system|string|
|**mode**  <br>*required*|Mound file or directory writeable (RW) or readonly (RO)|enum (RW, RO)|


<a name="mounts"></a>
### Mounts
Optional Volumes for Docker containers.

*Type* : < [Mount](#mount) > array


<a name="port"></a>
### Port
Port of the host and the container system.


|Name|Description|Schema|
|---|---|---|
|**container**  <br>*required*|Port of the container.|number|
|**host**  <br>*required*|Port of the host system.|number|


<a name="ports"></a>
### Ports
Ports container could shoult map to the host system.


|Name|Schema|
|---|---|
|**port**  <br>*required*|[Port](#port)|


<a name="task"></a>
### Task

|Name|Description|Schema|
|---|---|---|
|**cmd**  <br>*optional*|A command specified for a task|string|
|**container**  <br>*optional*||[Container](#container)|
|**cores**  <br>*optional*|Cores a Task should use.|number|
|**cputime**  <br>*optional*|Cpu time a task is allowed to use.|number|
|**memory**  <br>*optional*|Memory used by a task.|number|
|**stderr**  <br>*optional*|Path to a file for stderr a task could produce.|string|
|**stdout**  <br>*optional*|Path to a file for stdout a task could produce.|string|
|**user**  <br>*required*|Task specification representing a specific Task a Framework should run.|string|





