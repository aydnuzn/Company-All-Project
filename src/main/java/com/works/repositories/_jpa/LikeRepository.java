package com.works.repositories._jpa;

import com.works.entities.LikeManagement;
import com.works.entities.projections.LikeInfo;
import com.works.entities.projections.LikeInfoAccordingToCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface LikeRepository extends JpaRepository<LikeManagement, Integer> {


    @Query(value = "select sum(score) as totalScore, product_id as productID from like_management group by product_id", nativeQuery = true)
    List<LikeInfo> findAllLikes();

    @Query(value = "SELECT\n" +
            "\tcustomer.id AS cid,\n" +
            "\tCONCAT( customer.NAME, \" \", customer.surname ) AS cname,\n" +
            "\tscore,\n" +
            "\tproduct_id as pid\n" +
            "FROM\n" +
            "\tlike_management\n" +
            "\tINNER JOIN `user` AS customer \n" +
            "WHERE\n" +
            "\tcustomer.id = like_management.customer_id", nativeQuery = true)
    List<LikeInfoAccordingToCustomer> findAllLikesAccordingToCustomer();

    Optional<LikeManagement> findByCustomer_IdEqualsAndProduct_IdEquals(Integer id, Integer id1);


}
