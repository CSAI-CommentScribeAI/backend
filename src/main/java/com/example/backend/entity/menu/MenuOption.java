package com.example.backend.entity.menu;

import com.example.backend.entity.TimeZone;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuOption extends TimeZone {

    @Id @GeneratedValue
    @Column(name = "menuOption_id")
    private Long id;

    @Column(nullable = false)
    private String optionName;

    @Column(nullable = false)
    private int optionPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuOptionTitle_id")
    @JsonIgnore
    private MenuOptionTitle menuOptionTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
}
