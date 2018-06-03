**Quickstart:**

1. Download "target/fileanalyser.jar"
2. Start with: java -jar fileanalyser.jar

**Build project**

To build the project enter the project root directory and execute:
```
mvn clean install
``` 
Then run it with:
```
cd target/
java -jar fileanalyser-0.0.1-SNAPSHOT.jar
``` 

**Example requests:**

```
curl -v "localhost:8080/evaluation?url=http://localhost:8080/examplefile/valid"
```

**Useful Links:**

- [Healthstatus](localhost:8080/actuator/health)
- [Swagger Docs](localhost:8080/swaggerui)

