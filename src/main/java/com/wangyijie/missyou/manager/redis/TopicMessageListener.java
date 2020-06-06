package com.wangyijie.missyou.manager.redis;

import com.wangyijie.missyou.bo.OrderMessageBO;
import com.wangyijie.missyou.service.CouponBackService;
import com.wangyijie.missyou.service.OrderCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class TopicMessageListener implements MessageListener {
    @Autowired
    private OrderCancelService orderCancelService;

    @Autowired
    private CouponBackService couponBackService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        String expiredKey = new String(body);
        String topic = new String(channel);

        OrderMessageBO orderMessageBO = new OrderMessageBO(expiredKey);
        orderCancelService.cancel(orderMessageBO);
        couponBackService.returnBack(orderMessageBO);
    }
}
