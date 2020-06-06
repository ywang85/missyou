package com.wangyijie.missyou.service;

import com.wangyijie.missyou.bo.OrderMessageBO;
import com.wangyijie.missyou.exception.http.ServerErrorException;
import com.wangyijie.missyou.model.Order;
import com.wangyijie.missyou.model.OrderSku;
import com.wangyijie.missyou.repository.OrderRepository;
import com.wangyijie.missyou.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderCancelService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuRepository skuRepository;

    @Transactional
    public void cancel(OrderMessageBO orderMessageBO) {
        if (orderMessageBO.getOrderId() <= 0) {
            throw new ServerErrorException(9999);
        }
        cancel(orderMessageBO.getOrderId());
    }

    private void cancel(Long oid) {
        Optional<Order> orderOptional = orderRepository.findById(oid);
        Order order = orderOptional.orElseThrow(() -> {
            throw new ServerErrorException(9999);
        });
        int res = orderRepository.cancelOrder(oid);
        if (res != 1) {
            return;
        }
//        order.getSnapItems().forEach(i -> skuRepository.reduceStock(i.getId(), i.getCount().longValue()));
        for (OrderSku orderSku : order.getSnapItems()) {
            skuRepository.reduceStock(orderSku.getId(), orderSku.getCount().longValue());
        }
    }
}
