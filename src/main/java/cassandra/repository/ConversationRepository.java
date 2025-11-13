package cassandra.repository;

import cassandra.entity.Conversation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConversationRepository extends CrudRepository<Conversation, UUID> {
}
