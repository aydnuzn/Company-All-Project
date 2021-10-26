package com.works.entities.survey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.works.entities.listener.BaseEntityNotCompany;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class SurveySelection extends BaseEntityNotCompany<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Length(min = 2, max = 20)
    @NotNull
    @NotEmpty
    @Column(length = 20)
    private String survey_selection_title;
    private Integer survey_selection_score;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "survey_id")
    private Survey survey;
}