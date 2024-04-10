package com.example.backend.menu.dto;

import com.example.backend.menu.entity.Group;
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
