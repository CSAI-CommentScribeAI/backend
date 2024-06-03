package com.example.backend.repository.openAI;

import com.example.backend.entity.openAI.ReplySave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplySaveRepository extends JpaRepository<ReplySave, Long> {
}
