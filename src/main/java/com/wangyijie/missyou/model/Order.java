package com.wangyijie.missyou.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wangyijie.missyou.dto.OrderAddressDTO;
import com.wangyijie.missyou.util.GenericAndJson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Where(clause = "delete_time is null")
@Table(name = "`Order`")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalPrice;
    private Long totalCount;

    private String snapImg;
    private String snapTitle;
    private String snapItems;
    private String snapAddress;
    private String prepayId;
    private BigDecimal finalTotalPrice;
    private Integer status;

    public OrderAddressDTO getSnapAddress() {
        if (snapAddress == null) {
            return null;
        }
        return GenericAndJson.jsonToObject(snapAddress, new TypeReference<OrderAddressDTO>() {});
    }

    public void setSnapAddress(OrderAddressDTO address) {
        snapAddress = GenericAndJson.objectToJson(address);
    }

    public void setSnapItems(List<OrderSku> orderSkuList) {
        if (orderSkuList.isEmpty()) {
            return;
        }
        snapItems = GenericAndJson.objectToJson(orderSkuList);
    }

    public List<OrderSku> getSnapItems() {
        return GenericAndJson.jsonToObject(snapItems, new TypeReference<List<OrderSku>>(){});
    }
}
