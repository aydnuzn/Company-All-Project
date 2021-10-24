package com.works.business._controllers.adminpanel;

import org.springframework.stereotype.Service;

@Service
public class LikeControllerBusiness {
    final String rvalue = "adminpanel/like/";

    public String likeList() {
        return rvalue + "likelist";
    }
}
