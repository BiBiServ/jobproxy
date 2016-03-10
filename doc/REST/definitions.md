## Definitions
### Port

Port of the host and the container system.

|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|host|Port of the host system.|true|number||
|container|Port of the container.|true|number||


### TContainer

Optional Docker container.

|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|image|Docker image specification|true|string||
|cmd|Command a Docker container should execute|false|string||
|ports||false|TPorts||


### TPorts

Ports container could shoult map to the host system.

|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|port||true|Port||


### Task
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|user|Task specification representing a specific Task a Framework should run.|true|string||
|cores|Cores a Task should use.|false|number||
|memory|Memory used by a task.|false|number||
|cputime|Cpu time a task is allowed to use.|false|number||
|stdout|Path to a file for stdout a task could produce.|false|string||
|stderr|Path to a file for stderr a task could produce.|false|string||
|cmd|A command specified for a task|false|string||
|container||false|TContainer||


