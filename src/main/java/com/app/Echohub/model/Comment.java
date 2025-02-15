package com.app.Echohub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    @Field("post_id")
    @Indexed
    private String postId;

    @DBRef(lazy = true)
    @Field("user")
    @Indexed
    private User user;

    private String content;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder.Default
    @JsonIgnore
    private Set<String> likes = new HashSet<>();
}
