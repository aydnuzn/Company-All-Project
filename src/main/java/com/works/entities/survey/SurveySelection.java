package com.works.entities.survey;

import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SurveySelection extends BaseEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String survey_selection_title;
    private Integer survey_selection_score;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "survey_id")
    private Survey survey;



}
