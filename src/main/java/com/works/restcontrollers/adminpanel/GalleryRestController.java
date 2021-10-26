package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.GalleryRestControllerBusiness;
import com.works.utils.REnum;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("rest/admin/gallery")
public class GalleryRestController {

    final GalleryRestControllerBusiness business;

    public GalleryRestController(GalleryRestControllerBusiness business) {
        this.business = business;
    }


    @GetMapping("/list")
    public Map<REnum, Object> galleryList() {
        return business.galleryList();
    }

    @GetMapping("/{stIndex}")
    public Map<REnum, Object> galleryUpdateShow(@PathVariable String stIndex) {
        return business.galleryUpdateShow(stIndex);
    }

}
