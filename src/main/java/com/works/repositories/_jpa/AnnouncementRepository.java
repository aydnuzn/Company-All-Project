package com.works.repositories._jpa;

import com.works.entities.Announcement;
import com.works.entities.projections.DashboardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

    @Query(value = "SELECT t1.totalAdvertisement, t2.totalContent, t3.totalCustomer, t4.totalActiveCustomer, t5.totalPassiveCustomer, t6.totalAnnouncement, t7.totalActiveAnn, t8.totalPassiveAnn, t9.totalProduct, t10.totalLike, t11.totalSurvey, t12.totalOrder FROM\n" +
            "(Select COUNT(*) as totalAdvertisement From advertisement Where company_id = ?1) as t1,\n" +
            "(Select COUNT(*) as totalContent From content Where company_id = ?1) as t2,\n" +
            "(Select COUNT(*) as totalCustomer From `user` as u INNER JOIN users_roles as ur ON u.id = ur.user_id Where ur.role_id = 3 and u.company_id = ?1) as t3,\n" +
            "(Select COUNT(*) as totalActiveCustomer From `user` as u INNER JOIN users_roles as ur ON u.id = ur.user_id Where ur.role_id = 3 and u.cu_status = 1 and u.company_id = ?1) as t4,\n" +
            "(Select COUNT(*) as totalPassiveCustomer From `user` as u INNER JOIN users_roles as ur ON u.id = ur.user_id Where ur.role_id = 3 and u.cu_status = 2 and u.company_id = ?1) as t5,\n" +
            "(Select COUNT(*) as totalAnnouncement From announcement as ann INNER JOIN announcement_category as anncat ON ann.announcement_category_id = anncat.id Where company_id= ?1) as t6,\n" +
            "(Select COUNT(*) as totalActiveAnn From announcement as ann INNER JOIN announcement_category as anncat ON ann.announcement_category_id = anncat.id Where ann.ann_type = 1 and company_id= ?1) as t7,\n" +
            "(Select COUNT(*) as totalPassiveAnn From announcement as ann INNER JOIN announcement_category as anncat ON ann.announcement_category_id = anncat.id Where ann.ann_type = 2 and company_id= ?1) as t8,\n" +
            "(Select COUNT(*) as totalProduct From product Where company_id = ?1) as t9,\n" +
            "(SELECT COUNT(*) as totalLike FROM like_management as lm INNER JOIN `user` as u ON lm.customer_id = u.id WHERE u.company_id = ?1) as t10,\n" +
            "(Select COUNT(*) as totalSurvey From survey Where company_id = ?1) as t11,\n" +
            "(Select COUNT(*) as totalOrder From orders Where company_id = ?1) as t12", nativeQuery = true)
    Optional<DashboardInfo> getViewDashboard(Integer companyIndex);

}
