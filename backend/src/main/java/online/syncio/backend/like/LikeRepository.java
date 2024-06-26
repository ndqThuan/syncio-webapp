package online.syncio.backend.like;

import online.syncio.backend.idclass.PkUserPost;
import online.syncio.backend.post.Post;
import online.syncio.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, PkUserPost> {

    Like findFirstByPost(Post post);

    Like findFirstByUser(User user);

    Optional<Like> findByPostIdAndUserId(UUID postId, UUID userId);

    Long countByPostId(UUID postId);

}
