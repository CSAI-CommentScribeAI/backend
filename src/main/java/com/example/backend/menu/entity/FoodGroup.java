package com.example.backend.menu.entity;

import com.example.backend.root.IdentifiableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "food_group_mapping")
@NoArgsConstructor
public class FoodGroup extends IdentifiableEntity {
    @ManyToOne(cascade = CascadeType.PERSIST)

    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;


    @Builder
    public FoodGroup(Food food, Group group) {
        this.food = food;
        this.group = group;
    }
}
