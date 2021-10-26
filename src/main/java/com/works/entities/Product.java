package com.works.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.works.entities.categories.ProductCategory;
import com.works.entities.images.ProductImage;
import com.works.entities.listener.BaseEntity;
import com.works.entities.security.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Product extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "products_categories",
            joinColumns = @JoinColumn(
                    name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "category_id", referencedColumnName = "id"))
    private List<ProductCategory> pr_categories;

    @Column(length = 50)
    private String pr_name;

    @Column(length = 100)
    private String pr_brief_description;

    @Column(length = 500)
    private String pr_description;

    private String pr_price;
    private Integer pr_type;
    private Integer pr_campaign;
    private Integer pr_campaign_name;

    @Column(length = 500)
    private String pr_campaign_description;

    @Column(length = 500)
    private String pr_address;

    private Integer pr_latitude;
    private Integer pr_longitude;

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.DETACH)
    private List<ProductImage> images;

}
