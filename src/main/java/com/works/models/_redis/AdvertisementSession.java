package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;


@Data
@RedisHash("sessionadvertisement")
public class AdvertisementSession {

    @Id
    private String id;
    private String adv_title;
    private String adv_shown_number;
    private String adv_date_begin;
    private String adv_date_end;
    private String adv_image;
    private String adv_width;//genişlik
    private String adv_height;
    private String adv_link;
    @Indexed
    private String companyname = Util.theCompany == null ? "":Util.theCompany.getCompany_name() ;

}
