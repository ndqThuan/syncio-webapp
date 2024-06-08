package online.syncio.backend.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import online.syncio.backend.post.Post;
import online.syncio.backend.post.PostDTO;

import java.util.Set;
import java.util.UUID;

@Data
public class UserProfile {
    private UUID id;

    @NotNull
    @Size(max = 30)
    private String username;

    @Size(max = 1000)
    private String avtURL;

    private String bio;

    private Set<PostDTO> posts;

    private long followerCount;

    private long followingCount;

}
