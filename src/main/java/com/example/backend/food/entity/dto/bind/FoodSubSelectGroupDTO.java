package com.example.backend.food.entity.dto.bind;

import com.example.backend.food.entity.FoodSubSelectGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodSubSelectGroupDTO {
    @JsonProperty("id")
    private long id;

    @JsonProperty("groupName")
    private String groupName;

    @JsonProperty("multiselect")
    private boolean multiselect;

    @JsonProperty("required")
    private boolean required;


    public FoodSubSelectGroupDTO(FoodSubSelectGroup group) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.multiselect = group.isMultiSelect();
        this.required = group.isRequired();
    }
}
