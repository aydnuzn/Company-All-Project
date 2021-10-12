package com.works.entities;

import com.works.entities.listener.BaseEntity;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Content extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @NotEmpty
    private String content_title;
    @NotNull
    @NotEmpty
    private String content_brief_description;
    @NotNull
    @NotEmpty
    private String content_detailed_description;
    @NotNull
    private Integer content_status;
}
