package com.works.properties;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class GalleryImageInterlayer {
    @NotNull
    @Min(value = 1)
    private Integer gallery_title;

    @NotNull(message = "not null")
    MultipartFile gallery_image_files;

    @NotNull
    @NotEmpty
    private String gallery_image_title;
}
