package com.wangyijie.missyou.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangyijie.missyou.exception.http.ParameterException;
import com.wangyijie.missyou.model.User;
import com.wangyijie.missyou.repository.UserRepository;
import com.wangyijie.missyou.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 1. code换取用户的openid
 * 2. user id -> uid
 * 3. 注册 -> openid 写入 user / 查询user uid
 * 4. uid写入jwt
 * 5. jwt-> 小程序
 */
@Service
public class WxAuthenticationService {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Value("${wx.code2session}")
    private String code2SessionUrl;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.appsecret}")
    private String appsecret;

    public String code2Session(String code) {
        String url = MessageFormat.format(code2SessionUrl, appid, appsecret, code);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> session = new HashMap<>();
        String sessionText = restTemplate.getForObject(url, String.class);
        try {
            session = mapper.readValue(sessionText, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return registerUser(session);
    }

    private String registerUser(Map<String, Object> session) {
        String openid = (String) session.get("openid");
        if (openid == null) {
            throw new ParameterException(20004);
        }
        User user = userRepository.findByOpenid(openid);
        if (user != null) {
            //todo 返回jwt令牌
            return JwtToken.makeToken(user.getId());
        }
        User currentUser = User.builder().openid(openid).build();
        userRepository.save(currentUser);
        // todo 返回jwt令牌
        Long userId = currentUser.getId();
        return JwtToken.makeToken(userId);
    }
}
