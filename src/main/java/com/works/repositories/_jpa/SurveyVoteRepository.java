package com.works.repositories._jpa;

import com.works.entities.survey.SurveyVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyVoteRepository extends JpaRepository<SurveyVote, Integer> {
}
