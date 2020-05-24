package com.wangyijie.missyou.sample.hero;

import com.wangyijie.missyou.sample.ISkill;
import org.springframework.stereotype.Component;

//@Component
public class Diana implements ISkill {
    public void q() {
        System.out.println("Diana q");
    }

    public void w() {
        System.out.println("Diana w");
    }

    public void e() {
        System.out.println("Diana e");
    }

    public void r() {
        System.out.println("Diana r");
    }
}
