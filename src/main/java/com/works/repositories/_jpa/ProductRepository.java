package com.works.repositories._jpa;

import com.works.entities.Product;
import com.works.entities.projections.ProductsOfCategoryInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByIdEqualsAndCompany_IdEqualsAllIgnoreCase(Integer productId, Integer companyId);

    @Query(value = "Select * From product\n" +
            "WHERE company_id = ?1 ORDER BY created_date DESC\n" +
            "LIMIT 0, 10", nativeQuery = true)
    List<Product> getLastTenProductItem(Integer companyId);

    @Query(value = "SELECT pc.category_id as categoryId, pc.product_id as productId, p.company_id as companyId FROM products_categories as pc\n" +
            "INNER JOIN product as p ON pc.product_id = p.id\n" +
            "Where pc.category_id = ?1 and p.company_id = ?2", nativeQuery = true)
    List<ProductsOfCategoryInfo> getProductsOfCategory(Integer categoryId, Integer companyId, Pageable pageable);

    @Query(value = "SELECT pc.category_id as categoryId, pc.product_id as productId, p.company_id as companyId FROM products_categories as pc\n" +
            "INNER JOIN product as p ON pc.product_id = p.id\n" +
            "Where pc.category_id = ?1 and p.company_id = ?2", nativeQuery = true)
    List<ProductsOfCategoryInfo> getProductsOfCategory(Integer categoryId, Integer companyId);
}
