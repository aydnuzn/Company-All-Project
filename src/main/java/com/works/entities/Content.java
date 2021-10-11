package com.works.entities;

import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Content extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String content_title;
    private String content_brief_description;
    private String content_detailed_description;
    private Integer content_status;
}
