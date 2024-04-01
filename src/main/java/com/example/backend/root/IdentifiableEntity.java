package com.example.backend.root;

import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
@Getter
public class IdentifiableEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;
}
