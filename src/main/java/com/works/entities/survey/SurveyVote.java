package com.works.entities.survey;

import com.works.entities.Customer;
import com.works.entities.listener.BaseEntity;
import com.works.entities.listener.BaseEntityNotCompany;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SurveyVote extends BaseEntityNotCompany<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "survey_selection_id")
    private SurveySelection surveySelection;
}