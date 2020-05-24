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
@Where(clause = "delete_time is null")
// 滚动的轮播图入口
public class Banner extends BaseEntity {
    @Id
    private Long id;
    private String name;
    private String description;
    private String title;
    private String img;

    @OneToMany
    @JoinColumn(name = "bannerId")
    private List<BannerItem> items;

}
