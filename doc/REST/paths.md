## Paths
### Tasks states
```
GET /jobproxy/state
```

#### Description

Returns task states.


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|Task IDs returned|No Content|


### Tasks state
```
POST /jobproxy/state
```

#### Description

The states endpoint accepts a task id.


#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|Task ID||true|string||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|Accepted Task ID|No Content|


#### Consumes

* application/json
* application/xml

### Tasks to be run
```
POST /jobproxy/submit
```

#### Description

The submit endpoint accepts a task definition which is used to run
on a fraemwork.


#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|Task||true|Task||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|Accepted Task|No Content|


#### Consumes

* application/json
* application/xml

