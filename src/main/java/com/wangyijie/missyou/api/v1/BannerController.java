package com.wangyijie.missyou.api.v1;

import com.wangyijie.missyou.core.interceptors.ScopeLevel;
import com.wangyijie.missyou.exception.http.NotFoundException;
import com.wangyijie.missyou.model.Banner;
import com.wangyijie.missyou.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banner")
@Validated
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @ScopeLevel()
    @GetMapping("/name/{name}")
    public Banner getByName(@PathVariable String name) {
        Banner banner = bannerService.getByName(name);
        if (banner == null) {
            throw new NotFoundException(30005);
        }
        return banner;
    }
//    @Autowired
//    private Diana diana;

//    @Autowired
//    private ISkill iSkill;

//    @Autowired
//    public BannerController(Diana diana) {
//        this.diana = diana;
//    }

    // host:port/v1/banner/test
//    @PostMapping("/test/{id}")
//    public String test(@PathVariable @Max(10) Integer id, @RequestBody @Validated PersonDTO person) {
////        iSkill.e();
//        System.out.println(person);
//        throw new ForbiddenException(10001);
////        return "Hello, 小逸!";
//    }

}
