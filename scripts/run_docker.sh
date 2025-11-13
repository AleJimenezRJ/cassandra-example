#!/usr/bin/env bash

set -e  # Exit on error
echo ""
echo "   Cassandra Cluster + Spring Boot    "
echo ""

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_ROOT"

echo "Building Spring Boot application..."
echo ""
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "Error: Maven build failed!"
    exit 1
fi
echo "Build completed successfully!"
echo ""

echo "Starting Cassandra cluster (3 nodes)..."
echo ""
cd docker

# Stop any existing containers
echo "Stopping existing containers..."
docker-compose down 2>/dev/null || true
echo ""

# Start the cluster
echo "Starting cassandra-node1 (seed node)..."
docker-compose up -d cassandra-node1

echo "Waiting for node1 to be healthy..."
sleep 60

echo "Starting cassandra-node2 (seed node)..."
docker-compose up -d cassandra-node2

echo "Waiting for node2 to be healthy..."
sleep 40

echo "Starting cassandra-node3..."
docker-compose up -d cassandra-node3

echo "Waiting for node3 to be healthy..."
sleep 40

echo ""
echo "Initializing database schema..."
docker-compose up -d cassandra-init

echo "Waiting for initialization to complete..."
sleep 30

echo ""
echo "Starting Spring Boot application..."
docker-compose up -d spring-boot-cassandra

echo "Waiting for application to start..."
sleep 15

echo ""
echo "Cluster started successfully!"
echo ""
echo "Cluster Status:"
docker exec -it cassandra-node1 nodetool status 2>/dev/null || echo "Run 'docker exec -it cassandra-node1 nodetool status' to check cluster"
echo ""
echo "Services:"
echo "  Cassandra Node 1: localhost:9042"
echo "  Cassandra Node 2: localhost:9043"
echo "  Cassandra Node 3: localhost:9044"
echo "  •Spring Boot API:  http://localhost:9003"
echo "  Swagger UI:       http://localhost:9003/swagger-ui.html"
echo ""
echo "Useful commands:"
echo "  Check cluster:    docker exec -it cassandra-node1 nodetool status"
echo "  CQL Shell:        docker exec -it cassandra-node1 cqlsh"
echo "  View logs:        docker logs spring-boot-cassandra"
echo "  Stop cluster:     cd docker && docker-compose down"
echo ""