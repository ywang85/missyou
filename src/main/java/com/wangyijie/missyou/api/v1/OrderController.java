package com.wangyijie.missyou.api.v1;

import com.wangyijie.missyou.core.LocalUser;
import com.wangyijie.missyou.core.interceptors.ScopeLevel;
import com.wangyijie.missyou.dto.OrderDTO;
import com.wangyijie.missyou.logic.OrderChecker;
import com.wangyijie.missyou.service.OrderService;
import com.wangyijie.missyou.vo.OrderIdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("")
    @ScopeLevel
    public OrderIdVO placeOrder(@RequestBody OrderDTO orderDTO) {
        Long uid = LocalUser.getUser().getId();
        OrderChecker orderChecker = orderService.isOk(uid, orderDTO);

    }
}
