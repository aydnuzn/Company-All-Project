package com.works.controllers.adminpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/like")
public class LikeController {

    final String rvalue = "adminpanel/like/";

    @GetMapping("/list")
    public String likeList() {
        return "adminpanel/like/likelist";
    }

}
