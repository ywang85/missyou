package com.wangyijie.missyou.service;

import com.wangyijie.missyou.bo.OrderMessageBO;
import com.wangyijie.missyou.core.enumeration.OrderStatus;
import com.wangyijie.missyou.exception.http.ServerErrorException;
import com.wangyijie.missyou.model.Order;
import com.wangyijie.missyou.repository.OrderRepository;
import com.wangyijie.missyou.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponBackService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Transactional
    public void returnBack(OrderMessageBO bo) {
        Long couponId = bo.getCouponId();
        Long userId = bo.getUserId();
        Long orderId = bo.getOrderId();

        if (couponId == -1) {
            return;
        }
        Order order = orderRepository.findFirstByUserIdAndId(userId, orderId);
        if (order == null) {
            throw new ServerErrorException(9999);
        }
        if (order.getStatus().equals(OrderStatus.UNPAID.getValue())
        || order.getStatus().equals(OrderStatus.CANCELED.getValue())) {
            int result = userCouponRepository.returnBack(couponId, userId);
            if (result != 1) {
                return;
            }
        }
    }
}
