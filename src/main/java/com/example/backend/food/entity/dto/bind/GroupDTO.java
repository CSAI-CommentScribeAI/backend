package com.example.backend.food.entity.dto.bind;

import com.example.backend.food.entity.Group;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GroupDTO {
    @JsonProperty("id")
    private final long id;

    @JsonProperty("name")
    private final String name;


    public GroupDTO(Group group) {
        this.id = group.getId();
        this.name = group.getName();
    }
}
