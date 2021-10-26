package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.ConstantRestControllerBusiness;
import com.works.utils.REnum;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ConstantRestController {

    final ConstantRestControllerBusiness business;

    public ConstantRestController(ConstantRestControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("/getXDistricts/{stIndex}")
    public Map<REnum, Object> getXDistricts(@PathVariable String stIndex) {
        return business.getXDistricts(stIndex);
    }

    @GetMapping("/getLogo")
    public Map<String, Object> getLogo() {
        return business.getLogo();
    }

    @DeleteMapping("/delete/gallery/{stIndex}")
    public Map<REnum, Object> galleryDelete(@RequestBody @PathVariable String stIndex) {
        return business.galleryDelete(stIndex);
    }

    @DeleteMapping("/delete/gallery/{galleryId}/{imageId}")
    public Map<REnum, Object> galleryImageDelete(@RequestBody @PathVariable String galleryId, @PathVariable String imageId) {
        return business.galleryImageDelete(galleryId, imageId);
    }
}
