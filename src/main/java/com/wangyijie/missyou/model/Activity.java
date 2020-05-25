package com.wangyijie.missyou.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Where(clause = "delete_time is null and online = 1")
public class Activity extends BaseEntity {
    @Id
    private Long id;
    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private String remark;
    private Byte online;
    private String entranceImg;
    private String internalTopImg;
    private String name;

    @OneToMany
    @JoinColumn(name = "activityId")
    private List<Coupon> couponList;
}
