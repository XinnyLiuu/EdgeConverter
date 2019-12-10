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

## Release Notes
- `CreateDDLButtonListener.getOutputClasses()`
	- Created a new objOutput with no arguments 
	- Updated resultClass to search within the edgeconvert package
	- Changed “EdgeConvert.” To “edgeconvert.” 

- `CreateDDLMySQL`
    - We assessed the way in which the table attributes were being added to the MySQL query and noticed that there was a trailing comma being added to the final attributes of the each table. We refactored it to check if we had reached the final attribute and only added the comma portion if we were not on the final attribute. 

- We also generally refactored the code to be much more readable, mainly in the front end. We separated the blob front end class that was handed to us into multiple classes which are logically separated and handle only their relevant action listeners.

- The monolith that used to be `EdgeConvertGUI` has been broken up into separate class files that are isolated in their own packages. This promotes reusability, maintainability and overall separation of concerns for the application as a whole.
