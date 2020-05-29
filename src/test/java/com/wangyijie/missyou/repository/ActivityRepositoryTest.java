package com.wangyijie.missyou.repository;

import com.wangyijie.missyou.model.Activity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityRepositoryTest {
    @Autowired
    private ActivityRepository activityRepository;

    @Test
    public void findByCouponListId() {
        Activity activity = activityRepository.findByCouponListId(3L);
        System.out.println(activity.getId());
    }
}