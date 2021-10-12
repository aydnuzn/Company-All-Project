package com.works.repositories._jpa;

import com.works.entities.survey.SurveySelection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveySelectionRepository extends JpaRepository<SurveySelection,Integer> {
}
