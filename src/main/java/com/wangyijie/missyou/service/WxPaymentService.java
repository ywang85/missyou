package com.wangyijie.missyou.service;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.github.wxpay.sdk.WangWxPayConfig;
import com.wangyijie.missyou.core.LocalUser;
import com.wangyijie.missyou.exception.http.ForbiddenException;
import com.wangyijie.missyou.exception.http.NotFoundException;
import com.wangyijie.missyou.exception.http.ParameterException;
import com.wangyijie.missyou.exception.http.ServerErrorException;
import com.wangyijie.missyou.model.Order;
import com.wangyijie.missyou.repository.OrderRepository;
import com.wangyijie.missyou.util.CommonUtil;
import com.wangyijie.missyou.util.HttpRequestProxy;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPaymentService {
    @Value("${missyou.order.pay-callback-host}")
    private String payCallbackHost;

    @Value("${missyou.order.pay-callback-path}")
    private String payCallbackPath;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private static WangWxPayConfig wangWxPayConfig = new WangWxPayConfig();

    public Map<String, String> preOrder(Long oid) {
        Long uid = LocalUser.getUser().getId();
        Order order = orderRepository.findFirstByUserIdAndId(uid, oid);
        if (order == null) {
            throw new NotFoundException(50009);
        }
        if (order.needCancel()) {
            throw new ForbiddenException(50010);
        }

        WXPay wxPay = assembleWxPayConfig();
        Map<String, String> params = makePreOrderParams(order.getFinalTotalPrice(), order.getOrderNo());

        Map<String, String> wxOrder;
        try {
            wxOrder = wxPay.unifiedOrder(params);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
        // prepay_id 保存微信订单号，延迟支付
        if (unifiedOrderSuccess(wxOrder)) {
            orderService.updateOrderPrepayId(order.getId(), wxOrder.get("prepay_id"));
        }
        // wx.requestPayment 小程序拉起微信支付
        return makePaySignature(wxOrder);
    }

    private Map<String, String> makePaySignature(Map<String, String> wxOrder) {
        Map<String, String> wxPayMap = new HashMap<>();
        String packages = "prepay_id" + wxOrder.get("prepay_id");
        wxPayMap.put("appId", WxPaymentService.wangWxPayConfig.getAppID());
        wxPayMap.put("timeStamp", CommonUtil.timeStamp10());
        wxPayMap.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
        wxPayMap.put("package", packages);
        wxPayMap.put("signType", "HMAC-SHA256");

        String sign;
        try {
            sign = WXPayUtil.generateSignature(wxPayMap, WxPaymentService.wangWxPayConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }

        Map<String, String> miniPayParams = new HashMap<>();
        miniPayParams.put("paySign", sign);
        miniPayParams.putAll(wxPayMap);
        miniPayParams.remove("appId");
        return miniPayParams;
    }

    private boolean unifiedOrderSuccess(Map<String, String> wxOrder) {
        if (!wxOrder.get("return_code").equals("SUCCESS") || !wxOrder.get("result_code").equals("SUCCESS")) {
            throw new ParameterException(10007);
        }
        return true;
    }

    private Map<String, String> makePreOrderParams(BigDecimal serverFinalPrice, String orderNo) {
        Map<String, String> data = new HashMap<>();
        data.put("body", "Sleeve"); // 商品标题
        data.put("out_trade_no", orderNo);
        data.put("device_info", "Sleeve");
        data.put("fee_type", "CNY");
        data.put("trade_type", "JSAPI");
        data.put("total_fee", CommonUtil.yuanToFenPlainString(serverFinalPrice));
        data.put("open_id", LocalUser.getUser().getOpenid());
        data.put("spbill_create_ip", HttpRequestProxy.getRemoteRealIp());
        data.put("notify_url", payCallbackHost + payCallbackPath);
        return data;
    }

    public WXPay assembleWxPayConfig() {
        WXPay wxPay;
        try {
            wxPay = new WXPay(WxPaymentService.wangWxPayConfig);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
        return wxPay;
    }
}
