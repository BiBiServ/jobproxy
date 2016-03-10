## Paths
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

