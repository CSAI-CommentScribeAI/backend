package com.example.backend.entity.menu;

import com.example.backend.entity.TimeZone;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuConnect extends TimeZone { // Menu와 MenuOptionTitle을 이어준다.

    @Id @GeneratedValue
    @Column(name = "menuConnect_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "menuOptionTitle_id")
    private MenuOptionTitle menuOptionTitle;

}
