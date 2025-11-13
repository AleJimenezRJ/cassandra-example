#!/usr/bin/env bash

set -e  # Exit on error

echo ""
echo "   Stopping the Cassandra Cluster and Spring Boot Application         "
echo ""

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_ROOT/docker"

echo "Stopping all services..."
docker-compose down

echo ""
echo "All services stopped successfully!"
echo ""
echo "To remove volumes (delete all data), run:"
echo "   cd docker && docker-compose down -v"
echo ""
