# Deployment 
For our deployment strategy, we have included Maven support for EdgeConverter.

The choice for Maven is to ensure that the project compiles and that test cases completely pass before packaging it into a JAR file.

## Requirements
- Maven 
- Java 8+
- Linux (preferred)

## Maven 
### Installation
For Linux (Ubuntu):
```
sudo apt update
sudo apt install maven
```

Further instructions can be found __[here](https://maven.apache.org/install.html)__.

### Usage
In the same directory as `pom.xml`, run:
```
mvn clean install && cd target/ && \
java -jar EdgeConverter-1.0-SNAPSHOT-jar-with-dependencies.jar
```
The above commands calls maven to package the application into a jar file and simultaneously executes the jar file.

#### Setup Edge File
Click `File` > `Open Edge File` > Navigate to the directory of `EdgeConverter` > Select `Courses.edg` > Click `Open`.

#### Set Ouput File Definition Location
Click `Options` > `Set Output File Definition Location` > Navigate to the directory of `EdgeConverter` > Select `target` directory > Select `classes` directory > Select `edgeconvert` directory > Click `Open`.

#### Create DDL
- Ensure the step above is completed.

Click `Create DDL` > Select a Product (`MySQL`) > Enter the database name > Select the directory to save the exported `.sql` file > Click `Save`.

## MySQL
To load SQL file into MySQL:
```sql
mysql -u username –-password=your_password database_name < file.sql 

OR

mysql
source file.sql;
```
