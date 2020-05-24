package com.wangyijie.missyou;

import com.wangyijie.missyou.sample.HeroConfiguration;
import com.wangyijie.missyou.sample.ISkill;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

//@ComponentScan
@Import(HeroConfiguration.class)
public class LOLApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(LOLApplication.class).web(WebApplicationType.NONE).run(args);
        ISkill iSkill = (ISkill) context.getBean("camille");
        iSkill.e();
    }
}
