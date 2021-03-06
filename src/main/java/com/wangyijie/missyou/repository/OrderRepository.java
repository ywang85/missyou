package com.wangyijie.missyou.repository;

import com.wangyijie.missyou.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByExpiredTimeGreaterThanAndStatusAndUserId(Date now, Integer status, Long uid, Pageable pageable);

    Page<Order> findByUserId(Long uid, Pageable pageable);

    Page<Order> findByUserIdAndStatus(Long uid, Integer status, Pageable pageable);

    Order findFirstByUserIdAndId(Long uid, Long orderId);

    Order findFirstByOrderNo(String orderNo);

    @Modifying
    @Query("update Order o set o.status = :status where o.orderNo = :orderNo")
    int updateStatusByOrderNo(String orderNo, Integer status);

    // 一定是未支付的订单
    @Modifying
    @Query("update Order o set o.status = 5 where o.status = 1 and o.id = :oid")
    int cancelOrder(Long oid);
}
