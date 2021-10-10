package com.works.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.works.entities.categories.ProductSubCategory;
import com.works.entities.categories.ProductTopCategory;
import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

//Arrayleri taşıyamazsa interlayer kurulacak.

@Data
@Entity
public class Product extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonBackReference
    @ManyToMany(mappedBy = "products", cascade = CascadeType.DETACH)
    private List<ProductTopCategory> pr_top_categories;

    @JsonBackReference
    @ManyToMany(mappedBy = "products", cascade = CascadeType.DETACH)
    private List<ProductSubCategory> pr_sub_categories;

    private String pr_name;
    private String pr_brief_description;
    private String pr_description;
    private String pr_price;
    private Integer pr_type;
    private Integer pr_campaign;
    private Integer pr_campaign_name;
    @Column(length = 500)
    private String pr_campaign_description;
    @Column(length = 500)
    private String pr_address;
    private String pr_latitude;
    private String pr_longitude;

    @OneToMany(mappedBy = "product", cascade = CascadeType.DETACH)
    private List<Image> images;

}
