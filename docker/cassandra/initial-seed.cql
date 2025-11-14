-- Create the keyspace, similar to a CREATE DATABASE in SQL. The replication factor adds redundancy to the db
CREATE KEYSPACE IF NOT EXISTS spring_boot_cassandra WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'}  AND durable_writes = true;

USE spring_boot_cassandra;

-- Table for storing conversations metadata
CREATE TABLE conversations (
   conversation_id uuid PRIMARY KEY,
   conversation_name text,
   created_at timestamp,
   participants set<text>,
   last_message_time timestamp
);

-- Table for storing chat messages (wide column design)
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

-- Insert sample conversations
INSERT INTO conversations (conversation_id, conversation_name, created_at, participants, last_message_time)
   VALUES (11111111-1111-1111-1111-111111111111, 'Team Project Discussion', '2025-11-12 08:00:00', {'alice', 'bob', 'charlie'}, '2025-11-12 14:30:00');

INSERT INTO conversations (conversation_id, conversation_name, created_at, participants, last_message_time)
   VALUES (22222222-2222-2222-2222-222222222222, 'Weekend Plans', '2025-11-11 10:00:00', {'alice', 'diana'}, '2025-11-12 09:15:00');

INSERT INTO conversations (conversation_id, conversation_name, created_at, participants, last_message_time)
   VALUES (33333333-3333-3333-3333-333333333333, 'Work Updates', '2025-11-10 09:00:00', {'bob', 'charlie', 'eve'}, '2025-11-12 16:45:00');

-- Insert sample messages for conversation 1 (Team Project Discussion)
INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (11111111-1111-1111-1111-111111111111, now(), 'alice', 'Alice Smith', 'Hey team! How is the Cassandra integration going?', '2025-11-12 08:15:00', true);

INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (11111111-1111-1111-1111-111111111111, now(), 'bob', 'Bob Johnson', 'Going great! Just finished setting up the 3-node cluster.', '2025-11-12 08:17:00', true);

INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (11111111-1111-1111-1111-111111111111, now(), 'charlie', 'Charlie Brown', 'Awesome! I am working on the REST API endpoints now.', '2025-11-12 08:20:00', true);

INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (11111111-1111-1111-1111-111111111111, now(), 'alice', 'Alice Smith', 'Perfect! Let me know if you need help with anything.', '2025-11-12 08:25:00', true);

INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (11111111-1111-1111-1111-111111111111, now(), 'bob', 'Bob Johnson', 'Will do! Should we schedule a demo for Friday?', '2025-11-12 14:30:00', false);

-- Insert sample messages for conversation 2 (Weekend Plans)
INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (22222222-2222-2222-2222-222222222222, now(), 'alice', 'Alice Smith', 'Hey Diana! Are you free this weekend?', '2025-11-11 10:00:00', true);

INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (22222222-2222-2222-2222-222222222222, now(), 'diana', 'Diana Prince', 'Yes! What do you have in mind?', '2025-11-11 10:05:00', true);

INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (22222222-2222-2222-2222-222222222222, now(), 'alice', 'Alice Smith', 'Maybe hiking or checking out that new restaurant?', '2025-11-11 10:10:00', true);

INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (22222222-2222-2222-2222-222222222222, now(), 'diana', 'Diana Prince', 'The restaurant sounds great! Saturday at 7pm?', '2025-11-12 09:15:00', false);

-- Insert sample messages for conversation 3 (Work Updates)
INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (33333333-3333-3333-3333-333333333333, now(), 'bob', 'Bob Johnson', 'Quick update: deployment went smoothly!', '2025-11-12 09:00:00', true);

INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (33333333-3333-3333-3333-333333333333, now(), 'charlie', 'Charlie Brown', 'Great news! Any issues to report?', '2025-11-12 09:05:00', true);

INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (33333333-3333-3333-3333-333333333333, now(), 'bob', 'Bob Johnson', 'None so far. All systems running smoothly.', '2025-11-12 09:10:00', true);

INSERT INTO chat_messages (conversation_id, message_id, sender_id, sender_name, message_text, created_at, is_read)
   VALUES (33333333-3333-3333-3333-333333333333, now(), 'eve', 'Eve Wilson', 'Excellent work team!', '2025-11-12 16:45:00', false);


-- WIDE COLUMN TABLE: Sensor Metrics
-- This table demonstrates the power of Cassandra's wide column design.
-- Unlike SQL where you'd need multiple tables or rows, here we can store
-- MILLIONS of metrics for a single sensor in ONE partition.
-- The clustering columns (year, month, day, hour, recorded_at) create a 
-- natural time-series structure where each sensor can have unlimited metrics.

CREATE TABLE sensor_metrics (
   sensor_id text,
   year int,
   month int,
   day int,
   hour int,
   recorded_at timestamp,
   metric_type text,
   metric_value double,
   unit text,
   location text,
   status text,
   metadata map<text, text>,
                  -- Partition Key         Clustering Columns = WIDE columns
   PRIMARY KEY ((sensor_id, year, month), day, hour, recorded_at)
) WITH CLUSTERING ORDER BY (day DESC, hour DESC, recorded_at DESC)
  AND comment = 'Wide column design: Each sensor can have millions of time-series metrics in a single partition';

-- Insert sample data for sensor TEMP-001 (Temperature Sensor)
-- November 13, 2025 - Morning readings
INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('TEMP-001', 2025, 11, 13, 8, '2025-11-13 08:00:00', 'temperature', 22.5, 'celsius', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('TEMP-001', 2025, 11, 13, 8, '2025-11-13 08:15:00', 'temperature', 22.7, 'celsius', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('TEMP-001', 2025, 11, 13, 8, '2025-11-13 08:30:00', 'temperature', 23.1, 'celsius', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('TEMP-001', 2025, 11, 13, 8, '2025-11-13 08:45:00', 'temperature', 23.4, 'celsius', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('TEMP-001', 2025, 11, 13, 9, '2025-11-13 09:00:00', 'temperature', 23.8, 'celsius', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('TEMP-001', 2025, 11, 13, 9, '2025-11-13 09:15:00', 'temperature', 24.2, 'celsius', 'Server Room A', 'warning', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('TEMP-001', 2025, 11, 13, 9, '2025-11-13 09:30:00', 'temperature', 25.1, 'celsius', 'Server Room A', 'warning', {'device': 'DHT22', 'firmware': '1.2.3'});

-- November 12, 2025 - Previous day readings
INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('TEMP-001', 2025, 11, 12, 14, '2025-11-12 14:00:00', 'temperature', 21.8, 'celsius', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('TEMP-001', 2025, 11, 12, 14, '2025-11-12 14:30:00', 'temperature', 22.1, 'celsius', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('TEMP-001', 2025, 11, 12, 15, '2025-11-12 15:00:00', 'temperature', 22.4, 'celsius', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

-- Insert sample data for sensor HUMID-001 (Humidity Sensor)
INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('HUMID-001', 2025, 11, 13, 8, '2025-11-13 08:00:00', 'humidity', 45.2, 'percent', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('HUMID-001', 2025, 11, 13, 8, '2025-11-13 08:15:00', 'humidity', 46.1, 'percent', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('HUMID-001', 2025, 11, 13, 8, '2025-11-13 08:30:00', 'humidity', 47.5, 'percent', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('HUMID-001', 2025, 11, 13, 9, '2025-11-13 09:00:00', 'humidity', 48.8, 'percent', 'Server Room A', 'normal', {'device': 'DHT22', 'firmware': '1.2.3'});

-- Insert sample data for sensor CPU-001 (CPU Usage Sensor)
INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('CPU-001', 2025, 11, 13, 8, '2025-11-13 08:00:00', 'cpu_usage', 34.5, 'percent', 'Node-1', 'normal', {'host': 'cassandra-node-1', 'cores': '8'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('CPU-001', 2025, 11, 13, 8, '2025-11-13 08:05:00', 'cpu_usage', 42.1, 'percent', 'Node-1', 'normal', {'host': 'cassandra-node-1', 'cores': '8'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('CPU-001', 2025, 11, 13, 8, '2025-11-13 08:10:00', 'cpu_usage', 56.7, 'percent', 'Node-1', 'normal', {'host': 'cassandra-node-1', 'cores': '8'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('CPU-001', 2025, 11, 13, 8, '2025-11-13 08:15:00', 'cpu_usage', 78.9, 'percent', 'Node-1', 'warning', {'host': 'cassandra-node-1', 'cores': '8'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('CPU-001', 2025, 11, 13, 8, '2025-11-13 08:20:00', 'cpu_usage', 89.2, 'percent', 'Node-1', 'critical', {'host': 'cassandra-node-1', 'cores': '8'});

INSERT INTO sensor_metrics (sensor_id, year, month, day, hour, recorded_at, metric_type, metric_value, unit, location, status, metadata)
   VALUES ('CPU-001', 2025, 11, 13, 8, '2025-11-13 08:25:00', 'cpu_usage', 62.3, 'percent', 'Node-1', 'normal', {'host': 'cassandra-node-1', 'cores': '8'});