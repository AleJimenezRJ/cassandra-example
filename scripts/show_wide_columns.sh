#!/usr/bin/env bash

set -e  # Exit on error
echo ""
echo "   Showing Wide Columns in Cassandra    "
echo ""

echo "Displaying total metrics for sensor 'TEMP-001' in November 2025..."
docker exec -it cassandra-node1 cqlsh -e "USE spring_boot_cassandra; SELECT COUNT(*) as total_metrics_in_one_partition FROM sensor_metrics WHERE sensor_id = 'TEMP-001' AND year = 2025 AND month = 11;"

sleep 1

echo ""
echo "Now, see the difference when wide columns are not used. This is the count of conversations for a specific conversation_id..."
docker exec -it cassandra-node1 cqlsh -e "USE spring_boot_cassandra; SELECT COUNT(*) FROM conversations WHERE conversation_id = 11111111-1111-1111-1111-111111111111;"
