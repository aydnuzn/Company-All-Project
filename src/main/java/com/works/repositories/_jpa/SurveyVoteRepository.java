package com.works.repositories._jpa;

import com.works.entities.survey.SurveyVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SurveyVoteRepository extends JpaRepository<SurveyVote, Integer> {


    @Query(value = "SELECT\n" +
            "\tCOUNT(id)\n" +
            "FROM\n" +
            "\tsurvey_vote \n" +
            "WHERE\n" +
            "\tcustomer_id = ?1", nativeQuery = true)
    Optional<Integer> findSorveyVoteOld(Integer id);//Önceden oy vermiş mi?
}
