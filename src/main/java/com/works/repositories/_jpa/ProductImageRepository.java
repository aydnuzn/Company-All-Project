package com.works.repositories._jpa;

import com.works.entities.Product;
import com.works.entities.images.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProduct_IdEquals(Integer id);
}
