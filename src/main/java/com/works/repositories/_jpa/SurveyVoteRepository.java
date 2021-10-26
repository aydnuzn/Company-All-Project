package com.works.repositories._jpa;

import com.works.entities.survey.SurveyVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SurveyVoteRepository extends JpaRepository<SurveyVote, Integer> {


    @Query(value = "Select Count(*)   From survey_vote as sv \n" +
            "INNER JOIN survey_selection as ss ON sv.survey_selection_id = ss.id\n" +
            "Where customer_id = :customerId and survey_id = :surveyId",nativeQuery = true)
    Optional<Integer> findSorveyVoteOld(@Param("customerId") Integer sdfsdfds, @Param("surveyId") Integer sdfsddsf);
}
