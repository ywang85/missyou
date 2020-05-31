package com.wangyijie.missyou.repository;

import com.wangyijie.missyou.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
