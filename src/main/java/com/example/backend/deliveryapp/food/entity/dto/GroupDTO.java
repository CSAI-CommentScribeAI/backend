package com.example.backend.deliveryapp.food.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.backend.deliveryapp.food.entity.Group;
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
