package com.works.repositories._jpa;

import com.works.entities.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {
}
