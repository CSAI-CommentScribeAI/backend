package com.example.backend.repository.openAI;

import com.amazonaws.services.workmailmessageflow.model.GetRawMessageContentResult;
import com.example.backend.entity.openAI.LetterSave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterSaveRepository extends JpaRepository<LetterSave, Long> {

}
