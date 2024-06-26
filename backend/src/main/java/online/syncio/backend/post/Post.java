package online.syncio.backend.post;

import jakarta.persistence.*;
import lombok.Data;
import online.syncio.backend.comment.Comment;
import online.syncio.backend.like.Like;
import online.syncio.backend.report.Report;
import online.syncio.backend.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Table(name = "post")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class Post {
    @Id
    @Column(nullable = false, updatable = false)
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(columnDefinition = "text")
    private String caption;

    @ElementCollection
    private List<String> photos;

    @Column
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private Boolean flag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

//    Like
    @OneToMany(mappedBy = "post")
    private Set<Like> likes;

//    Comment
    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

//    Report
    @OneToMany(mappedBy = "post")
    private Set<Report> reports;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", caption='" + caption + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
