package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.LikeControllerBusiness;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/like")
public class LikeController {

    final LikeControllerBusiness business;


    public LikeController(LikeControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("/list")
    public String likeList() {
        return business.likeList();
    }
}
