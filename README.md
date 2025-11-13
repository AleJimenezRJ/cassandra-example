# Spring Boot + Apache Cassandra

## Credits and changes

The original [base code](https://github.com/reljicd/spring-boot-cassandra) belongs to user [reljicd](https://github.com/reljicd). All credit of that due to him. This version has been updated and refactored with:

- Compatibility with newer versions of Spring Boot, Swagger, Java, Maven and Docker
- Simplified package structure (`cassandra` instead of `com.reljicd`)
- 3-node distributed Cassandra cluster configuration
- One-command deployment script
- Endpoints for chats

## Project Description

This is an example application that demonstrates the integration of **Spring Boot** with **Apache Cassandra**, including a REST API documented with **Swagger UI**.

### Architecture

This project includes a **distributed Cassandra cluster** with 3 nodes:
- **High Availability**: Data is distributed across multiple nodes
- **Fault Tolerance**: The cluster continues operating even if one node fails
- **Load Balancing**: Spring Boot connects to all nodes for optimal performance
- **Data Replication**: Configured with NetworkTopologyStrategy for production readiness

## How to Run the Project

### Quick Start (Recommended)

Run the entire Cassandra cluster (3 nodes) + Spring Boot with a single command:

```bash
./scripts/run_docker.sh
```

This script will:
1. Compile the Spring Boot application with Maven
2. Start a 3-node Cassandra distributed cluster
3. Initialize the database schema
4. Launch the Spring Boot application with Swagger UI

**To stop the cluster:**

```bash
./scripts/stop_docker.sh
```

### Manual Setup

If you prefer to control each step:

```bash
# 1. Compile the project
mvn clean package -DskipTests

# 2. Start the services
cd docker
docker-compose up -d

# 3. View the logs (optional)
docker-compose logs -f
```

## Access to Interfaces

Once the services are started, the following interfaces are available:

| Service | URL | Description |
|----------|-----|-------------|
| **Swagger UI** | http://localhost:9003/swagger-ui.html | Graphical interface to test the API |
| **REST API** | http://localhost:9003/api/text_field_1/{value} | Main endpoint |
| **Actuator Health** | http://localhost:9003/health | Application health status |
| **Actuator Metrics** | http://localhost:9003/metrics | Application metrics |
| **Cassandra Node 1** | localhost:9042 | First Cassandra node (seed) |
| **Cassandra Node 2** | localhost:9043 | Second Cassandra node (seed) |
| **Cassandra Node 3** | localhost:9044 | Third Cassandra node |


## Testing the Chat API

### **Using Swagger UI (Recommended)**

1. Open in browser: http://localhost:9003/swagger-ui.html
2. Expand the **"chat-controller"** section
3. Try these endpoints:

#### Get All Conversations

- Click on **GET** `/api/chat/conversations`
- Click **"Try it out"** -> **"Execute"**
- See all available conversations

#### Get Messages from a Conversation
- Click on **GET** `/api/chat/conversations/{conversationId}/messages`
- Click **"Try it out"**
- Enter conversation ID: `11111111-1111-1111-1111-111111111111`
- Click **"Execute"**
- See all messages in that conversation (demonstrates **wide column pattern**)

#### Get Latest Messages (with limit)
- Click on **GET** `/api/chat/conversations/{conversationId}/messages/latest`
- Enter conversation ID: `11111111-1111-1111-1111-111111111111`
- Set limit: `3` (to get only the 3 most recent messages)
- Click **"Execute"**

### **Using cURL from Terminal**

```bash
# Get all conversations
curl http://localhost:9003/api/chat/conversations

# Get all messages from a conversation (wide column demo)
curl http://localhost:9003/api/chat/conversations/11111111-1111-1111-1111-111111111111/messages

# Get latest 3 messages
curl "http://localhost:9003/api/chat/conversations/11111111-1111-1111-1111-111111111111/messages/latest?limit=3"

# Get a specific conversation
curl http://localhost:9003/api/chat/conversations/22222222-2222-2222-2222-222222222222
```

### **Sample Conversation IDs**

- `11111111-1111-1111-1111-111111111111` - Team Project Discussion (5 messages)
- `22222222-2222-2222-2222-222222222222` - Weekend Plans (4 messages)
- `33333333-3333-3333-3333-333333333333` - Work Updates (4 messages)

### Wide Column Pattern Demo

The chat system demonstrates Cassandra's wide column capability:
- Each conversation can contain thousands of messages in a single partition
- Messages are stored efficiently with conversation_id as partition key
- Messages are automatically sorted by message_id (timeuuid) in descending order
- Perfect for pagination and "infinite scroll" chat interfaces

## Data Structure and Query-Oriented Design

### Understanding Cassandra's Design Philosophy

Unlike relational databases that normalize data and use joins, Cassandra is designed around the queries you need to perform. This is called query-oriented design or query-driven modeling.

### Table Design: Chat Messaging System

This project demonstrates two fundamental Cassandra patterns:

#### 1. Conversations Table (Simple Primary Key)

```
CREATE TABLE conversations (
   conversation_id uuid PRIMARY KEY,
   conversation_name text,
   created_at timestamp,
   participants set<text>,
   last_message_time timestamp
);
```

Structure breakdown:
- conversation_id: Partition key that determines which node stores this row
- Each conversation is a single row with all metadata
- The set<text> type stores multiple participants efficiently without a join table
- Query pattern: Retrieve one conversation by ID or scan all conversations

#### 2. Chat Messages Table (Wide Column Pattern)

```
CREATE TABLE chat_messages (
   conversation_id uuid,
   message_id timeuuid,
   sender_id text,
   sender_name text,
   message_text text,
   created_at timestamp,
   is_read boolean,
   PRIMARY KEY (conversation_id, message_id)
) WITH CLUSTERING ORDER BY (message_id DESC);
```

Structure breakdown:
- Partition key: conversation_id (groups all messages for one conversation together)
- Clustering key: message_id (timeuuid - sorts messages within the partition)
- All messages for a conversation are stored together on the same node
- Messages are automatically sorted in descending order (newest first)
- This is the "wide column" pattern: one partition can have thousands of columns (messages)

### Why This Design Works

Query-oriented benefits:
1. Getting all messages for a conversation: Single partition read, no joins needed
2. Getting latest N messages: Use LIMIT clause with automatic DESC ordering
3. Pagination: Efficiently fetch chunks of messages using message_id as cursor
4. High write throughput: Appending new messages is an O(1) operation
5. Scalability: Each conversation can grow to millions of messages without performance degradation

### Primary Key Composition

The compound primary key (conversation_id, message_id) serves two purposes:
- Partition key (conversation_id): Determines data distribution across cluster nodes
- Clustering key (message_id): Determines sort order within each partition

This design ensures:
- All messages for a conversation live on the same nodes (data locality)
- Messages are pre-sorted by time (no ORDER BY overhead at query time)
- Range queries are efficient (get messages between two timestamps)

### Data Replication Strategy

The keyspace uses SimpleStrategy with replication factor 3:
```
CREATE KEYSPACE spring_boot_cassandra 
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'};
```

This means:
- Every piece of data is stored on 3 different nodes
- If one node fails, data remains available from the other two
- Reads can be served from any of the 3 replica nodes
- Consistency level can be tuned per query (ONE, QUORUM, ALL)

### Using the Browser Directly

Open in browser:
```
http://localhost:9003/api/chat/conversations
http://localhost:9003/api/chat/conversations/11111111-1111-1111-1111-111111111111/messages
```



## Useful Commands

### View Container Status
```bash
docker ps
```

### View Logs of Specific Container

```bash
# Spring Boot
docker logs spring-boot-cassandra -f

# Cassandra nodes
docker logs cassandra-node1 -f
docker logs cassandra-node2 -f
docker logs cassandra-node3 -f
```

### Check Cassandra Cluster Status

```bash
# View cluster status
docker exec -it cassandra-node1 nodetool status

# Connect to Cassandra with CQL shell
docker exec -it cassandra-node1 cqlsh

# View cluster information
docker exec -it cassandra-node1 nodetool ring
```

### Stop Services

```bash
# Using the script (recommended)
./scripts/stop_docker.sh

# Or manually
cd docker && docker-compose down

# To remove all data (volumes)
cd docker && docker-compose down -v
```

### Restart Services
```bash
docker compose -f docker/docker-compose.yml restart
```

### **Clean Everything and Start Fresh**
```bash
docker compose -f docker/docker-compose.yml down -v
docker system prune -f
./scripts/run_docker_compose.sh
```


## Configuration

### **Configuration File:** `src/main/resources/application.properties`

```properties
server.port=9003
spring.data.cassandra.contact-points=localhost
spring.data.cassandra.keyspace-name=spring_boot_cassandra
```

### Environment Variables (Docker Compose):

```yaml
SPRING_DATA_CASSANDRA_CONTACT_POINTS: cassandra
```


## Troubleshooting

### Error: "Port 9003 already in use

```bash
# Check which process is using the port
sudo lsof -i :9003

# Stop the container
docker stop spring-boot-cassandra
```

### Error: "Cannot connect to Cassandra"

```bash
# Verify that Cassandra is running
docker ps | grep cassandra

# Check Cassandra logs
docker logs cassandra

# Restart Cassandra
docker restart cassandra
```

### Error: "Tests failing during build"

The script already includes `-DskipTests` to avoid this issue.

### Containers not starting

```bash
# Clean and restart
docker compose -f docker/docker-compose.yml down
docker system prune -f
mvn clean package -DskipTests
docker compose -f docker/docker-compose.yml up --build -d
```


## Running Tests

This probably does not work as of now because it's not being developed.

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

- Spring Boot 2.7.18
- Apache Cassandra 3.11
- Spring Data Cassandra
- OpenAPI 3.0 specification (instead of Swagger 2.0)
- Spring Security (disabled for development)
- Swagger UI 2.9.2
- Docker & Docker Compose
- Actuator (monitoring and metrics)
- Automatic database initialization


