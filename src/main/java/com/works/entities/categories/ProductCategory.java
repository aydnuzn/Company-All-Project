package com.works.entities.categories;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.works.entities.Product;
import com.works.entities.listener.BaseEntity;
import com.works.entities.security.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class ProductCategory extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 40)
    private String pr_title;

    @OneToOne(  fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private ProductCategory productCategories;

    @ManyToMany(mappedBy = "pr_categories")
    private List<Product> products;



}
