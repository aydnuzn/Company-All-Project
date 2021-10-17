package com.works.properties;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data

public class LogoChangeInterlayer {
    @NotNull(message = "not null")
    MultipartFile change_logo_file;
}
