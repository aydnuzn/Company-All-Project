package com.works.repositories._jpa;

import com.works.entities.survey.SurveySelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveySelectionRepository extends JpaRepository<SurveySelection,Integer> {
    List<SurveySelection> findBySurvey_IdEquals(Integer id);

}
