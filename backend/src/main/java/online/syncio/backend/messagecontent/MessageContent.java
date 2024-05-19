package online.syncio.backend.messagecontent;

import jakarta.persistence.*;
import lombok.Data;
import online.syncio.backend.messageroom.MessageRoom;
import online.syncio.backend.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "message_content")
@EntityListeners(AuditingEntityListener.class)
@Data
public class MessageContent {
    @Id
    @Column(nullable = false, updatable = false)
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column
    @CreatedDate
    private LocalDateTime dateSent;

    @ManyToOne
    @JoinColumn(name = "message_room_id")
    private MessageRoom messageRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User user;

}