# WeeSvc SpringBoot
Implementation of the WeeSVC application using [Java](https://go.java/) and the [SpringBoot Reactive](https://spring.io/reactive) 
framework.

## Ingredients
The following external libraries were *directly* utilized in this project.

| Package       | Link                          | Description                                 |
| ---           | ---                           | ---                                         |
| Java          | https://go.java/              | Well...it's obvious isn't it?!              |
| SpringBoot    | https://spring.io/reactive    | Toolkit providing HTTP server and routing   |
| Tomcat        | https://tomcat.apache.org/    | Application server runtime                  |
| R2DBC         | https://r2dbc.io/             | Reactive API for relation databases         |
| R2DBC Migrate | https://nkonev.name/post/136  | Migration utility for databases using R2DBC |
| H2            | https://www.h2database.com/   | In-memory SQL database                      |

## Build
Builds are performed using [Gradle](https://gradle.org/) wrapper scripts provided in the project root.

### Getting Started
Run `./gradlew bootRun` from your command-line. This will build and start the service. 
Once fully started, you may verify the service by simply navigating to http://localhost:8080/greeting in your browser.

#### Docker
For those who do not have appropriate Java setup, [Docker](https://hub.docker.com/) is an option to build the application and run the application within a container.

The following command to build the application within a Linux container, then copies the resulting build into a _distroless_ image for execution.
```shell script
docker build -t github.com/weesvc/weesvc-springboot:0.0.1-SNAPSHOT .
```
Once the image is available, you can simply run the provided script to start the service.
```shell script
./docker-run.sh
```
> [!CAUTION]
> The `docker-run.sh` script will **not** maintain state between executions.
> This means each time you start the container, you will be starting with a freshly created H2 database.

## Using the Application
> [!TIP]
> Use the very cool [HTTPie](https://httpie.org/) application for testing locally from the command-line.

Execute a `GET` command to retrieve the available _places_ from the database.
```shell script
http GET :8080/api/places
```
```shell
HTTP/1.1 200 OK
Content-Type: application/json
transfer-encoding: chunked

[]
```
Add a _place_ into the database using a `POST` command.
```shell script
http POST :8080/api/places name=NISC description="NISC Lake St. Louis Office" latitude=38.7839 longitude=90.7878
```
```shell
HTTP/1.1 200 OK
Content-Length: 188
Content-Type: application/json

{
    "created_at": "2021-08-27T11:31:36.289798Z",
    "description": "NISC Lake St. Louis Office",
    "id": 1,
    "latitude": 38.7839,
    "longitude": 90.7878,
    "name": "NISC",
    "updated_at": "2021-08-27T11:31:36.289798Z"
}
```
Run the `GET` command again to retrieve _places_ which now include your newly added _place_!
```shell script
http GET :8080/api/places/1
```
```shell
HTTP/1.1 200 OK
Content-Length: 188
Content-Type: application/json

{
    "created_at": "2021-08-27T11:31:36.289798Z",
    "description": "NISC Lake St. Louis Office",
    "id": 1,
    "latitude": 38.7839,
    "longitude": 90.7878,
    "name": "NISC",
    "updated_at": "2021-08-27T11:31:36.289798Z"
}
```
Use the `PATCH` command to update a specific value.  For example we'll update the `Description` as follows:
```shell script
http PATCH :8080/api/places/1 description="Lake St. Louis"
```
```shell
HTTP/1.1 200 OK
Content-Length: 176
Content-Type: application/json

{
    "created_at": "2021-08-27T11:31:36.289798Z",
    "description": "Lake St. Louis",
    "id": 1,
    "latitude": 38.7839,
    "longitude": 90.7878,
    "name": "NISC",
    "updated_at": "2021-08-27T11:33:13.948471Z"
}
```
This returns the newly "patched" version of the _place_.  Next we'll remove the row using the `DELETE` method.
```shell script
http DELETE :8080/api/places/1
```
```shell
HTTP/1.1 200 OK
content-length: 0

```

## API Compliance
A core requirement for all _WeeSVC_ implementations is to implement the same API which are utilized for benchmark comparisons. To ensure compliance with the required API, [k6](https://k6.io/) is utilized within the [Workbench](https://github.com/weesvc/workbench) project.

To be a valid service, the following command MUST pass at 100%:
```
k6 run -e PORT=8080 https://raw.githubusercontent.com/weesvc/workbench/main/scripts/api-compliance.js
```
