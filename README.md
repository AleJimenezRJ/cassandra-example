# Spring Boot + Apache Cassandra

## Credits

The original [base code](https://github.com/reljicd/spring-boot-cassandra) belongs to user [reljicd](https://github.com/reljicd). I updated some files to be compatible with newer versions of java, maven and docker.

## Project Description

This is an example application that demonstrates the integration of **Spring Boot** with **Apache Cassandra**, including a REST API documented with **Swagger UI**.


## How to Run the Project

### **Option 1: Using Docker Compose (Recommended)**

```bash
# 1. Navigate to the project directory
cd /home/alejandro/Documents/Cassandra/spring-boot-cassandra

# 2. Execute the script
./scripts/run_docker_compose.sh
```

### **Option 2: Using Docker Compose Manually**

```bash
# 1. Compile the project
mvn clean package -DskipTests

# 2. Start the services
docker compose -f docker/docker-compose.yml up --build -d

# 3. View the logs (optional)
docker compose -f docker/docker-compose.yml logs -f
```

## Access to Interfaces

Once the services are started, the following interfaces are available:

| Service | URL | Description |
|----------|-----|-------------|
| **Swagger UI** | http://localhost:9003/swagger-ui.html | Graphical interface to test the API |
| **REST API** | http://localhost:9003/api/text_field_1/{value} | Main endpoint |
| **Actuator Health** | http://localhost:9003/health | Application health status |
| **Actuator Metrics** | http://localhost:9003/metrics | Application metrics |
| **Cassandra** | localhost:9042 | Cassandra database |


## Testing the API

### **Using Swagger UI (Recommended)**

1. Open in browser: http://localhost:9003/swagger-ui.html
2. Expand the **"example-table-controller"** section
3. Click on the **GET** endpoint `/api/text_field_1/{textField1}`
4. Click on the **"Try it out"** button
5. Enter a value in the `textField1` field (example: `text_field_1`)
6. Click on **"Execute"**
7. The response with Cassandra data will be displayed

### **Using cURL from Terminal**

```bash
# Query data with text_field_1 = "text_field_1"
curl http://localhost:9003/api/text_field_1/text_field_1

# Expected response:
# [{"textField1":"text_field_1","textField2":"text_field_2","intField1":1,"intField2":2}]
```

### **Using the Browser Directly**

Open in browser:
```
http://localhost:9003/api/text_field_1/text_field_1
```



## Useful Commands

### **View Container Status**
```bash
docker ps
```

### **View Logs of Specific Container**
```bash
# Spring Boot
docker logs spring-boot-cassandra -f

# Cassandra
docker logs cassandra -f
```

### **Stop Services**
```bash
docker compose -f docker/docker-compose.yml down
```

### **Restart Services**
```bash
docker compose -f docker/docker-compose.yml restart
```

### **Clean Everything and Start Fresh**
```bash
docker compose -f docker/docker-compose.yml down -v
docker system prune -f
./scripts/run_docker_compose.sh
```


## Data Structure

### **Keyspace:** `spring_boot_cassandra`

### **Table:** `example_table`

| Field | Type | Description |
|-------|------|-------------|
| `text_field_1` | TEXT | Primary key (Partition Key) |
| `text_field_2` | TEXT | Clustering key |
| `int_field_1` | INT | Clustering key |
| `int_field_2` | INT | Clustering key |

### **Initial Data:**
```sql
INSERT INTO example_table (text_field_1, text_field_2, int_field_1, int_field_2)
VALUES ('text_field_1', 'text_field_2', 1, 2);
```


## Configuration

### **Configuration File:** `src/main/resources/application.properties`

```properties
server.port=9003
spring.data.cassandra.contact-points=localhost
spring.data.cassandra.keyspace-name=spring_boot_cassandra
```

### **Environment Variables (Docker Compose):**

```yaml
SPRING_DATA_CASSANDRA_CONTACT_POINTS: cassandra
```


## Troubleshooting

### **Error: "Port 9003 already in use"**
```bash
# Check which process is using the port
sudo lsof -i :9003

# Stop the container
docker stop spring-boot-cassandra
```

### **Error: "Cannot connect to Cassandra"**
```bash
# Verify that Cassandra is running
docker ps | grep cassandra

# Check Cassandra logs
docker logs cassandra

# Restart Cassandra
docker restart cassandra
```

### **Error: "Tests failing during build"**
The script already includes `-DskipTests` to avoid this issue.

### **Containers not starting**
```bash
# Clean and restart
docker compose -f docker/docker-compose.yml down
docker system prune -f
mvn clean package -DskipTests
docker compose -f docker/docker-compose.yml up --build -d
```


## Available Endpoints

### **Main API**

| Method | Endpoint | Description | Example |
|--------|----------|-------------|---------|
| GET | `/api/text_field_1/{textField1}` | Search by text_field_1 | `/api/text_field_1/text_field_1` |

### **Actuator Endpoints**

| Endpoint | Description |
|----------|-------------|
| `/health` | Application health status |
| `/info` | Application information |
| `/metrics` | System metrics |
| `/env` | Environment variables |
| `/beans` | Spring beans |
| `/mappings` | Endpoint mappings |



## Running Tests

```bash
# Run all tests
mvn test

# Run specific tests
mvn test -Dtest=ExampleTableRepositoryTest
```


## Building the JAR

```bash
# Build the executable JAR
mvn clean package

# The JAR is generated at:
# target/spring-boot-cassandra-0.0.1-SNAPSHOT.jar

# Run the JAR directly (requires Cassandra running)
java -jar target/spring-boot-cassandra-0.0.1-SNAPSHOT.jar
```


## Important Notes

1. **Default Credentials**: No authentication is required (it is disabled in `SpringSecurityConfig.java`)

2. **Startup Time**: 
   - Cassandra: ~30 seconds
   - Spring Boot: ~10 additional seconds
   - Total: ~40-45 seconds

3. **Persistence**: Data in Cassandra does not persist between restarts (no volumes configured)

4. **Default Port**: 9003 (configurable in `application.properties`)



## Useful Links

- **Swagger UI**: http://localhost:9003/swagger-ui.html
- **API Docs JSON**: http://localhost:9003/v2/api-docs
- **Health Check**: http://localhost:9003/health


## Support

If any issues are encountered:

1. Check the logs: `docker logs spring-boot-cassandra`
2. Verify that all containers are running: `docker ps`
3. Refer to the "Troubleshooting" section above


## Implemented Features

- Spring Boot 1.5.8
- Apache Cassandra 3.11
- Spring Data Cassandra
- Spring Security (disabled for development)
- Swagger UI 2.9.2
- Docker & Docker Compose
- Actuator (monitoring and metrics)
- Automatic database initialization


