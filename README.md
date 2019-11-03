# MONITORING

## Setup
1. Clone 
2. Run Gradle on **build.gradle** 
3. Spring application class is in **src.main.java.server** package under **Application.class**

##
If Gradle tasks run successfully, run the Spring application.

The rest server should by default run on http://localhost:8080/

**API**:

* http://localhost:8080/start
    * REQUEST: [POST]
    * PARAMETERS: [url, interval]
    * RESPONSE: 200 on success monitoring start, 400 on unsuccessful start

* http://localhost:8080/stop
    * REQUEST: [POST]
    * PARAMETERS: [url]
    * RESPONSE: 200 on success monitoring stop, 204 for server already stopped / never started

* http://localhost:8080/overview
    * REQUEST: [GET]
    * PARAMETERS: 
    * RESPONSE: 200 
    * HEADERS: Content-Type:application/json