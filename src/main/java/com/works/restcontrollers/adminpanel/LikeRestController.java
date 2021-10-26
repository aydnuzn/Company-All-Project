package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.LikeRestControllerBusiness;
import com.works.utils.REnum;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("rest/admin/like")
public class LikeRestController {

    final LikeRestControllerBusiness business;

    public LikeRestController(LikeRestControllerBusiness business) {
        this.business = business;
    }

    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> likeListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        return business.likeListSearch(stSearchKey, stIndex);
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> likeList(@RequestBody @PathVariable String stIndex) {
        return business.likeList(stIndex);
    }

    //ALL LIKES
    @GetMapping("/allLikeList/{stIndex}")
    public Map<REnum, Object> allLikeList(@RequestBody @PathVariable String stIndex) {
        return business.allLikeList(stIndex);
    }

    //ALL LIKES ACCORDING TO CUSTOMER
    @GetMapping("/allLikeList/tocustomer")
    public Map<REnum, Object> allLikeList() {
        return business.allLikeList();
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> surveyDelete(@RequestBody @PathVariable String stIndex) {
        return business.surveyDelete(stIndex);
    }

    @GetMapping("/productLike/{stProduct}/{stScore}")
    public Map<REnum, Object> productLike(@PathVariable String stProduct, @PathVariable String stScore) {
        return business.productLike(stProduct, stScore);
    }
}
