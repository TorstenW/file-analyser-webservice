# Overview

This application provides a REST-API which expects one or more URLs.
It will try to download the file from that URL and extract some information from it if it has the correct format.

It will determine:
Who held the most speeches in the year 2013?
Who held the most speeches about the topic "Innere Sicherheit"?
Who held the speeches with the lowest aggregate word count?

This information will be returned in JSON-Format.

```json
{
  "mostSpeeches": null,
  "mostSecurity": "Alexander Abel",
  "leastWordy": "Caesare Collins"
}
```

# Build project

To build the project enter the project root directory and execute:

```
./gradlew clean build
```

Then run it with:

```
java -jar build/libs/fileanalyser-0.0.1-SNAPSHOT.jar
``` 

# Example requests:

```
curl -v "localhost:8080/evaluation?url=http://localhost:8080/examplefile/valid"
```

# Useful Links:

- [[Healthstatus]](http://localhost:8080/actuator/health)

