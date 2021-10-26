package com.works.entities.categories;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.works.entities.Product;
import com.works.entities.listener.BaseEntity;
import com.works.entities.security.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
public class ProductCategory extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 40, message = "Kategori ismi an az 2, en fazla 40 karakter olabilir")
    @Column(unique = true, length = 40)
    private String pr_title;

    @OneToOne(  fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private ProductCategory productCategories;

    @JsonBackReference
    @ManyToMany(mappedBy = "pr_categories")
    private List<Product> products;

}
