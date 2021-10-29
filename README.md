# Build project

To build the project enter the project root directory and execute:

```
mvn clean install
``` 

**Build with gradle doesn't work at the moment because gradle doesn't support Java 17**

Then run it with:

```
cd target/
java -jar fileanalyser-0.0.1-SNAPSHOT.jar
``` 

# Example requests:

```
curl -v "localhost:8080/evaluation?url=http://localhost:8080/examplefile/valid"
```

# Useful Links:

- [[Healthstatus]](http://localhost:8080/actuator/health)
- [[Swagger Docs]](http://localhost:8080/swagger-ui/index.html)

