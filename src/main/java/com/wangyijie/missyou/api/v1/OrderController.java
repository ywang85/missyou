package com.wangyijie.missyou.api.v1;

import com.wangyijie.missyou.bo.PageCounter;
import com.wangyijie.missyou.core.LocalUser;
import com.wangyijie.missyou.core.interceptors.ScopeLevel;
import com.wangyijie.missyou.dto.OrderDTO;
import com.wangyijie.missyou.logic.OrderChecker;
import com.wangyijie.missyou.model.Order;
import com.wangyijie.missyou.service.OrderService;
import com.wangyijie.missyou.util.CommonUtil;
import com.wangyijie.missyou.vo.OrderIdVO;
import com.wangyijie.missyou.vo.OrderSimplifyVO;
import com.wangyijie.missyou.vo.PagingDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Value("${missyou.order.pay-time-limit}")
    private Long payTimeLimit;

    @PostMapping("")
    @ScopeLevel
    public OrderIdVO placeOrder(@RequestBody OrderDTO orderDTO) {
        Long uid = LocalUser.getUser().getId();
        OrderChecker orderChecker = orderService.isOk(uid, orderDTO);

        Long oid = orderService.placeOrder(uid, orderDTO, orderChecker);
        return new OrderIdVO(oid);
    }

    @ScopeLevel
    @GetMapping("/status/unpaid")
    public PagingDozer getUnpaid(@RequestParam(defaultValue = "0") Integer start,
                                             @RequestParam(defaultValue = "10") Integer count) {
        PageCounter page = CommonUtil.convertToPageParameter(start, count);
        Page<Order> orderPage = orderService.getUnpaid(page.getPage(), page.getCount());
        PagingDozer<Order, OrderSimplifyVO> pagingDozer = new PagingDozer<>(orderPage, OrderSimplifyVO.class);
//        pagingDozer.getItems().forEach(o -> ((OrderSimplifyVO) o).setPeriod(payTimeLimit));

        for (Object o : pagingDozer.getItems()) {
            ((OrderSimplifyVO) o).setPeriod(payTimeLimit);
        }
        return pagingDozer;
    }
}
