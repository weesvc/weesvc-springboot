# WeeSvc SpringBoot
Implementation of the WeeSVC application using [Java](https://go.java/) and the [SpringBoot Reactive](https://spring.io/reactive) 
framework.

## Ingredients
The following external libraries were *directly* utilized in this project.

| Package       | Link                          | Description                                 |
| ---           | ---                           | ---                                         |
| Java          | https://go.java/              | Well...it's obvious isn't it?!              |
| SpringBoot    | https://spring.io/reactive    | Toolkit providing HTTP server and routing   |
| R2DBC         | https://r2dbc.io/             | Reactive API for relation databases         |
| R2DBC Migrate | https://nkonev.name/post/136  | Migration utility for databases using R2DBC |
| H2            | https://www.h2database.com/   | In-memory SQL database                      |

## Build
Builds are performed using [Gradle](https://gradle.org/) wrapper scripts provided in the project root.

### Getting Started
Run `./gradlew bootRun` from your command-line. This will build and start the service. Once fully started, you may verify
the service by simply navigating to http://localhost:8080/greeting in your browser.

## Using the Application
:point_up: TIP: Use the very cool [HTTPie](https://httpie.org/) application for testing locally from the command-line.

Execute a `GET` command to retrieve the available _places_ from the database.
```shell script
http GET localhost:8080/api/places
```
```
HTTP/1.1 200 OK
Content-Length: 2
Content-Type: application/json
Date: Sat, 25 Jan 2020 05:33:57 GMT

[]
```
Add a _place_ into the database using a `POST` command.
```shell script
http POST localhost:8080/api/places name=NISC description="NISC Lake St. Louis Office" latitude=38.7839 longitude=90.7878
```
```
HTTP/1.1 200 OK
Content-Length: 8
Content-Type: application/json
Date: Sat, 25 Jan 2020 05:34:08 GMT

{
    "id": 1
}
```
Run the `GET` command again to retrieve _places_ which now include your newly added _place_!
```shell script
http GET localhost:8080/api/places/1
```
```
HTTP/1.1 200 OK
Content-Length: 217
Content-Type: application/json
Date: Sat, 25 Jan 2020 05:34:18 GMT

[
    {
        "CreatedAt": "2020-01-24T23:34:08.491999-06:00",
        "DeletedAt": null,
        "Description": "NISC Lake St. Louis Office",
        "ID": 1,
        "Latitude": 38.7839,
        "Longitude": 90.7878,
        "Name": "NISC",
        "UpdatedAt": "2020-01-24T23:34:08.491999-06:00"
    }
]
```
Use the `PATCH` command to update a specific value.  For example we'll update the `Description` as follows:
```shell script
http PATCH localhost:8080/api/places/1 description="Lake St. Louis"
```
```
HTTP/1.1 200 OK
Content-Length: 203
Content-Type: application/json
Date: Sat, 25 Jan 2020 18:13:13 GMT

{
    "CreatedAt": "2020-01-24T23:34:08.491999-06:00",
    "DeletedAt": null,
    "Description": "Lake St. Louis",
    "ID": 1,
    "Latitude": 38.7839,
    "Longitude": 90.7878,
    "Name": "NISC",
    "UpdatedAt": "2020-01-25T12:13:13.351201-06:00"
}
```
This returns the newly "patched" version of the _place_.  Next we'll remove the row using the `DELETE` method.
```shell script
http DELETE localhost:8080/api/places/1
```
```
HTTP/1.1 200 OK
Content-Length: 21
Content-Type: application/json
Date: Sat, 25 Jan 2020 18:15:16 GMT

{
    "message": "removed"
}
```
