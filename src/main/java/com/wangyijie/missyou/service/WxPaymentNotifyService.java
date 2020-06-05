package com.wangyijie.missyou.service;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.wangyijie.missyou.core.enumeration.OrderStatus;
import com.wangyijie.missyou.exception.http.ServerErrorException;
import com.wangyijie.missyou.model.Order;
import com.wangyijie.missyou.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class WxPaymentNotifyService {
    @Autowired
    private WxPaymentService wxPaymentService;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void processPayNotify(String data) {
        Map<String, String> dataMap;
        try {
            dataMap = WXPayUtil.xmlToMap(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
        WXPay wxPay = wxPaymentService.assembleWxPayConfig();
        boolean valid;
        try {
            valid = wxPay.isResponseSignatureValid(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
        if (!valid) {
            throw new ServerErrorException(9999);
        }
        String returnCode = dataMap.get("return_code");
        String orderNo = dataMap.get("out_trade_no");
        String resultCode = dataMap.get("result_code");

        if (!returnCode.equals("SUCCESS")) {
            throw new ServerErrorException(9999);
        }
        if (!resultCode.equals("SUCCESS")) {
            throw new ServerErrorException(9999);
        }
        if (orderNo == null) {
            throw new ServerErrorException(9999);
        }
        deal(orderNo);
    }

    private void deal(String orderNo) {
        Order order = orderRepository.findFirstByOrderNo(orderNo);
        if (order == null) {
            throw new ServerErrorException(9999);
        }
        int res = -1;
        if (order.getStatus().equals(OrderStatus.UNPAID.getValue())
                || order.getStatus().equals(OrderStatus.CANCELED.getValue())) {
            res = orderRepository.updateStatusByOrderNo(orderNo, OrderStatus.PAID.getValue());
        }
        if (res != 1) {
            throw new ServerErrorException(9999);
        }
    }
}
