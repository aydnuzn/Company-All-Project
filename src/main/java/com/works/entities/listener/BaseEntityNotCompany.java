package com.works.entities.listener;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntityNotCompany<T> {
    @ApiModelProperty(hidden = true)
    @CreatedBy
    @Column(name = "created_by")
    private T created_by;

    @ApiModelProperty(hidden = true)
    @CreatedDate
    @Column(name = "created_date")
    private Date created_date;

    @ApiModelProperty(hidden = true)
    @LastModifiedBy
    @Column(name = "Last_modified_by")
    private T last_modified_by;

    @ApiModelProperty(hidden = true)
    @LastModifiedDate
    @Column(name = "Last_modified_date")
    private Date last_modified_date;
}