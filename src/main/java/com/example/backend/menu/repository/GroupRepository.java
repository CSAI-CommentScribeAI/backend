package com.example.backend.menu.repository;

import com.example.backend.menu.entity.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, Long> {
}
