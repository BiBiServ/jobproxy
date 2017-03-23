# JobProxy


<a name="overview"></a>
## Overview
JobProxy REST API for running task independent of the framework


### Version information
*Version* : 0.1.0.beta.1.10


### License information
*License* : Apache 2.0  
*License URL* : http://www.apache.org/licenses/LICENSE-2.0.html  
*Terms of service* : https://github.com/BiBiServ/jobproxy




<a name="paths"></a>
## Paths

<a name="delete"></a>
### Delete a Task.
```
DELETE /v1/jobproxy/delete/{id}
```


#### Description
Delete a Task.


#### Parameters

|Type|Name|Schema|
|---|---|---|
|**Path**|**id**  <br>*required*|string|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**default**|successful operation|No Content|


<a name="ping"></a>
### Just a simple ping command.
```
GET /v1/jobproxy/ping
```


#### Description
Just a simple ping command.


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|successful operation|string|


#### Produces

* `text/plain`


<a name="stateget"></a>
### Returns  the state of all tasks.
```
GET /v1/jobproxy/state
```


#### Description
Returns  the state of all tasks in machine readable format (either xml or json 
     * depending on  request-header mime-type)


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|successful operation|[States](#states)|


#### Produces

* `application/json`
* `application/xml`


<a name="statepost"></a>
### Returns the state of one task.
```
GET /v1/jobproxy/state/{id}
```


#### Description
Returns the state of one task with given id in machine readable format.
     * (either xml or json depending on  request-header mime-type)


#### Parameters

|Type|Name|Schema|
|---|---|---|
|**Path**|**id**  <br>*required*|string|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|successful operation|[State](#state)|


#### Produces

* `application/json`
* `application/xml`


<a name="submit"></a>
### Submit a task
```
POST /v1/jobproxy/submit
```


#### Description
Submit a task


#### Parameters

|Type|Name|Schema|
|---|---|---|
|**Body**|**task**  <br>*optional*|[Task](#task)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|successful operation|string|


#### Consumes

* `application/json`
* `application/xml`


#### Produces

* `text/plain`




<a name="definitions"></a>
## Definitions

<a name="mount"></a>
### Mount

|Name|Description|Schema|
|---|---|---|
|**container**  <br>*required*|**Length** : `1 - 2147483647`|string|
|**host**  <br>*required*|**Length** : `1 - 2147483647`|string|
|**mode**  <br>*required*|**Length** : `1 - 2147483647`|string|


<a name="port"></a>
### Port

|Name|Schema|
|---|---|
|**container**  <br>*optional*|integer(int32)|
|**host**  <br>*optional*|integer(int32)|


<a name="state"></a>
### State

|Name|Schema|
|---|---|
|**code**  <br>*required*|string|
|**description**  <br>*optional*|string|
|**id**  <br>*required*|string|
|**stderr**  <br>*optional*|string|
|**stdout**  <br>*optional*|string|


<a name="states"></a>
### States

|Name|Schema|
|---|---|
|**state**  <br>*required*|< [State](#state) > array|


<a name="tcontainer"></a>
### TContainer

|Name|Description|Schema|
|---|---|---|
|**image**  <br>*required*|**Length** : `1 - 2147483647`|string|
|**mounts**  <br>*optional*||< [TMounts](#tmounts) > array|
|**ports**  <br>*optional*||< [TPorts](#tports) > array|


<a name="tmounts"></a>
### TMounts

|Name|Schema|
|---|---|
|**mount**  <br>*required*|[Mount](#mount)|


<a name="tports"></a>
### TPorts

|Name|Schema|
|---|---|
|**port**  <br>*required*|[Port](#port)|


<a name="task"></a>
### Task

|Name|Description|Schema|
|---|---|---|
|**cmd**  <br>*optional*||string|
|**container**  <br>*optional*||[TContainer](#tcontainer)|
|**cores**  <br>*optional*||integer(int32)|
|**cputime**  <br>*optional*||integer(int32)|
|**memory**  <br>*optional*||integer(int32)|
|**stderr**  <br>*optional*||string|
|**stdout**  <br>*optional*||string|
|**user**  <br>*required*|**Length** : `1 - 2147483647`|string|





