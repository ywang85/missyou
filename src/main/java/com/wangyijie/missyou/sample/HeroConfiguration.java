package com.wangyijie.missyou.sample;

import com.wangyijie.missyou.sample.condition.CamilleCondition;
import com.wangyijie.missyou.sample.condition.DianaCondition;
import com.wangyijie.missyou.sample.hero.Camille;
import com.wangyijie.missyou.sample.hero.Diana;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeroConfiguration {
    @Bean
//    @Conditional(CamilleCondition.class)
    @ConditionalOnProperty(value = "hero.condition", havingValue = "camille")
    public ISkill camille() {
        return new Camille();
    }

//    @Bean
////    @Conditional(DianaCondition.class)
//    @ConditionalOnProperty(value = "hero.condition", havingValue = "diana")
//    public ISkill diana() {
//        return new Diana();
//    }
}
