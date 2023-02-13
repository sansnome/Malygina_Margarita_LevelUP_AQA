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

public class PostData {
    private int id;
    @JsonProperty("user_id")
    private String userId;
    private String title;
    private String body;
}
