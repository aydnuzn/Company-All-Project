package com.works.repositories._redis;

import com.works.models._redis.SurveySelectionSession;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories
public interface SurveySelectionSessionRepository extends CrudRepository<SurveySelectionSession, String> {

    List<SurveySelectionSession> findBySurveyid(String survey_id);

}