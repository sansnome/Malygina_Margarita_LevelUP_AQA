package ru.levelup.at.homework6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@ToString

public class CreateCommentDataRequest {
    @JsonProperty("post_id")
    private String postId;
    private String name;
    private String email;
    private String body;
}
