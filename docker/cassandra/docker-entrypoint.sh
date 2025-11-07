#!/bin/bash
set -e

# Start Cassandra in the background
echo "Starting Cassandra..."
/usr/local/bin/docker-entrypoint.sh cassandra &

# Wait for Cassandra to be ready
echo "Waiting for Cassandra to be ready..."
CQL_READY=0
while [ $CQL_READY -eq 0 ]; do
    echo "Checking if Cassandra is ready..."
    cqlsh -e "describe keyspaces" > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        CQL_READY=1
        echo "Cassandra is ready!"
    else
        echo "Cassandra not ready yet, waiting..."
        sleep 5
    fi
done

# Run the initialization script
if [ -f /initial-seed.cql ]; then
    echo "Running initialization script..."
    cqlsh -f /initial-seed.cql
    echo "Initialization complete!"
fi

# Keep the container running by waiting for the Cassandra process
wait
			sed -ri 's/^(# )?('"$yaml"':).*/\2 '"$val"'/' "$CASSANDRA_CONFIG/cassandra.yaml"
		fi
	done

	for rackdc in dc rack; do
		var="CASSANDRA_${rackdc^^}"
		val="${!var}"
		if [ "$val" ]; then
			sed -ri 's/^('"$rackdc"'=).*/\1 '"$val"'/' "$CASSANDRA_CONFIG/cassandra-rackdc.properties"
		fi
	done
fi

# This snippet executes CQL files after Cassandra is initialized
for f in docker-entrypoint-initdb.d/*; do
  case "$f" in
    *.cql) echo "$0: running $f" && until cqlsh -f "$f"; do >&2 echo "Cassandra is unavailable - sleeping"; sleep 5; done & ;;
  esac
  echo
done

exec "$@"