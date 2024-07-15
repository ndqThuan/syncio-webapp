package online.syncio.backend.messagecontent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageContentRepository extends JpaRepository<MessageContent, UUID> {

    List<MessageContent> findByMessageRoomIdOrderByDateSentAsc(UUID messageRoomId);

    @Query("SELECT COUNT(m) FROM MessageContent m WHERE m.messageRoom.id = :messageRoomId AND m.dateSent > :dateSent AND m.user.id != :userId")
    Long countByMessageRoomIdAndDateSentAfterAndUserIdNot(UUID messageRoomId, LocalDateTime dateSent, UUID userId);

    Optional<MessageContent> findFirstByMessageRoomIdOrderByDateSentDesc(UUID messageRoomId);

}
