package com.works.entities.survey;

import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Survey extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @NotNull
    @Column(unique = true)
    private String survey_title;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.DETACH)
    private List<SurveySelection> surveySelections;
    
}
