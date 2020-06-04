package com.wangyijie.missyou.api.v1;

import com.wangyijie.missyou.core.interceptors.ScopeLevel;
import com.wangyijie.missyou.lib.WxNotify;
import com.wangyijie.missyou.service.WxPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Positive;
import java.io.InputStream;
import java.util.Map;

@RequestMapping("payment")
@RestController
@Validated
public class PaymentController {
    @Autowired
    private WxPaymentService wxPaymentService;

    @PostMapping("/pay/order/{id}")
    @ScopeLevel
    public Map<String, String> preWxOrder(@PathVariable(name = "id") @Positive Long oid) {
        return wxPaymentService.preOrder(oid);
    }

    @RequestMapping("/wx/notify")
    public String payCallback(HttpServletRequest request, HttpServletResponse response) {
        InputStream s;
        try {
            s = request.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return WxNotify.fail();
        }
        String xml;
        xml = WxNotify.readNotify(s);

        return null;
    }
}
