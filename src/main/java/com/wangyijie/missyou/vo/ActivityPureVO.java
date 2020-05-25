package com.wangyijie.missyou.vo;

import com.wangyijie.missyou.model.Activity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Setter
@Getter
public class ActivityPureVO {
    private Long id;
    private String title;
    private Date startTime;
    private Date endTime;
    private String remark;
    private String entranceImg;

    public ActivityPureVO(Activity activity) {
        BeanUtils.copyProperties(activity, this);
    }
}
