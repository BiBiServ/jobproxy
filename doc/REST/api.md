# JobProxy


<a name="overview"></a>
## Overview
JobProxy REST API for running task independent of the framework


### Version information
*Version* : 0.1.0


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

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**id**  <br>*required*||string||


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

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**id**  <br>*required*||string||


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

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Body**|**Task**  <br>*required*||[Task](#task)||


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Accepted Task|No Content|


#### Consumes

* `application/json`
* `application/xml`




<a name="definitions"></a>
## Definitions

<a name="port"></a>
### Port
Port of the host and the container system.


|Name|Description|Schema|
|---|---|---|
|**container**  <br>*required*|Port of the container.|number|
|**host**  <br>*required*|Port of the host system.|number|


<a name="tcontainer"></a>
### TContainer
Optional Docker container.


|Name|Description|Schema|
|---|---|---|
|**cmd**  <br>*optional*|Command a Docker container should execute|string|
|**image**  <br>*required*|Docker image specification|string|
|**ports**  <br>*optional*||[TPorts](#tports)|


<a name="tports"></a>
### TPorts
Ports container could shoult map to the host system.


|Name|Description|Schema|
|---|---|---|
|**port**  <br>*required*||[Port](#port)|


<a name="task"></a>
### Task

|Name|Description|Schema|
|---|---|---|
|**cmd**  <br>*optional*|A command specified for a task|string|
|**container**  <br>*optional*||[TContainer](#tcontainer)|
|**cores**  <br>*optional*|Cores a Task should use.|number|
|**cputime**  <br>*optional*|Cpu time a task is allowed to use.|number|
|**memory**  <br>*optional*|Memory used by a task.|number|
|**stderr**  <br>*optional*|Path to a file for stderr a task could produce.|string|
|**stdout**  <br>*optional*|Path to a file for stdout a task could produce.|string|
|**user**  <br>*required*|Task specification representing a specific Task a Framework should run.|string|





