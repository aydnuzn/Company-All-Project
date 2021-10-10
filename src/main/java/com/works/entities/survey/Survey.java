package com.works.entities.survey;

import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Survey extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String survey_title;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.DETACH)
    private List<SurveySelection> surveySelections;





}
