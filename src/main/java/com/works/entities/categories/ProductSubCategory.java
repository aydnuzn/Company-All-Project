package com.works.entities.categories;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.works.entities.Product;
import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class ProductSubCategory extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String pr_sub_title;

    private String pr_top_id;

    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "product_sub_category_products",
            joinColumns = @JoinColumn(name = "product_sub_category_id"),
            inverseJoinColumns = @JoinColumn(name = "products_id"))
    private List<Product> products;

}
