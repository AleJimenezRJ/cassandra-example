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