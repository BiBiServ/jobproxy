Scenario: Calling help
Given jobproxy is installed.
When I run jobproxy with the parameters:
-h
Then the exit status should be 0

Scenario: Start framework in background
Given jobproxy is installed.
When I start the jobproxy server with the parameters:
-f ExampleFramework
Then the GET request using the url http://localhost:9999/v1/jobproxy/ping should be successful

Scenario: Listing Frameworks
Given jobproxy is installed.
When I run jobproxy with the parameters:
-l
Then the exit status should be 0
And the output should contain the values DRMAA Local Chronos

Scenario: Use Configuration File
Given jobproxy is installed.
And I create a file config.properties with the contents:
serveruri=http://localhost:9998/
When I start the jobproxy server with the parameters:
-f ExampleFramework -p config.properties
Then the GET request using the url http://localhost:9998/v1/jobproxy/ping should be successful
