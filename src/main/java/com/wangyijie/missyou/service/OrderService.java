package com.wangyijie.missyou.service;

import com.wangyijie.missyou.core.LocalUser;
import com.wangyijie.missyou.core.enumeration.CouponStatus;
import com.wangyijie.missyou.core.enumeration.OrderStatus;
import com.wangyijie.missyou.core.money.IMoneyDiscount;
import com.wangyijie.missyou.dto.OrderDTO;
import com.wangyijie.missyou.dto.SkuInfoDTO;
import com.wangyijie.missyou.exception.http.ForbiddenException;
import com.wangyijie.missyou.exception.http.NotFoundException;
import com.wangyijie.missyou.exception.http.ParameterException;
import com.wangyijie.missyou.logic.CouponChecker;
import com.wangyijie.missyou.logic.OrderChecker;
import com.wangyijie.missyou.model.*;
import com.wangyijie.missyou.repository.CouponRepository;
import com.wangyijie.missyou.repository.OrderRepository;
import com.wangyijie.missyou.repository.SkuRepository;
import com.wangyijie.missyou.repository.UserCouponRepository;
import com.wangyijie.missyou.util.CommonUtil;
import com.wangyijie.missyou.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private SkuService skuService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private IMoneyDiscount iMoneyDiscount;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuRepository skuRepository;

    @Value("${missyou.order.max-sku-limit}")
    private int maxSkuLimit;

    @Value("${missyou.order.pay-time-limit}")
    private Long payTimeLimit;

    public OrderChecker isOk(Long uid, OrderDTO orderDTO) {
        if (orderDTO.getFinalTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParameterException(50011);
        }
        List<Long> skuIdList = orderDTO.getSkuInfoList().stream().map(SkuInfoDTO::getId).collect(Collectors.toList());
        List<Sku> skuList = skuService.getSkuListByIds(skuIdList);

        Long couponId = orderDTO.getCouponId();

        CouponChecker couponChecker = null;
        if (couponId != null) {
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException(40004));
            UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndCouponIdAndStatus(uid, couponId, CouponStatus.AVAILABLE.getValue());
            if (userCoupon == null) {
                throw new NotFoundException(50006);
            }
            couponChecker = new CouponChecker(coupon, iMoneyDiscount);
        }
        OrderChecker orderChecker = new OrderChecker(orderDTO, skuList, couponChecker, maxSkuLimit);
        orderChecker.isOk();
        return orderChecker;
    }

    @Transactional
    public Long placeOrder(Long uid, OrderDTO orderDTO, OrderChecker orderChecker) {
        Calendar now = Calendar.getInstance();

        Order order = new Order();
        order.setOrderNo(OrderUtil.makeOrderNo());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setFinalTotalPrice(orderDTO.getFinalTotalPrice());
        order.setUserId(uid);
        order.setTotalCount(orderChecker.getTotalCount().longValue());
        order.setSnapImg(orderChecker.getLeaderImg());
        order.setSnapTitle(orderChecker.getLeaderTitle());
        order.setStatus(OrderStatus.UNPAID.getValue());
        order.setSnapAddress(orderDTO.getAddress());
        order.setSnapItems(orderChecker.getOrderSkuList());
        order.setPlacedTime(now.getTime());
        order.setExpiredTime(CommonUtil.addSomeSeconds(now, payTimeLimit.intValue()).getTime());


        orderRepository.save(order);
        // 减库存
        reduceStock(orderChecker);
        // 核对优惠券
        if (orderDTO.getCouponId() != null) {
            writeOffCoupon(orderDTO.getCouponId(), order.getId(), uid);
        }
        // 加入消息队列
        return order.getId();
    }

    public Page<Order> getUnpaid(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();
        return orderRepository.findByExpiredTimeGreaterThanAndStatusAndUserId(new Date(), OrderStatus.UNPAID.getValue(), uid, pageable);
    }

    private void writeOffCoupon(Long couponId, Long oid, Long uid) {
        int result = userCouponRepository.writeOff(couponId, oid, uid);
        if (result != 1) {
            throw new ForbiddenException(40012);
        }
    }

    private void reduceStock(OrderChecker orderChecker) {
        List<OrderSku> orderSkuList = orderChecker.getOrderSkuList();
        for (OrderSku orderSku : orderSkuList) {
            int result = skuRepository.reduceStock(orderSku.getId(), orderSku.getCount().longValue());
            if (result != 1) {
                throw new ParameterException(50003);
            }
        }
    }
}
