package com.works.repositories._jpa;

import com.works.entities.categories.ProductCategory;
import com.works.entities.projections.AllMainProductCategoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    @Query(value = "SELECT id, pr_title, parent_id FROM product_category Where company_id = :company Order By parent_id", nativeQuery = true)
    List<AllMainProductCategoryInfo> getAllMainCategory(@Param("company") Integer company);

    List<ProductCategory> findByCompany_IdEquals(Integer id);

}
