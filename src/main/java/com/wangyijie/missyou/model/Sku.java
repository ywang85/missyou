package com.wangyijie.missyou.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wangyijie.missyou.util.GenericAndJson;
import com.wangyijie.missyou.util.ListAndJson;
import com.wangyijie.missyou.util.MapAndJson;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
@Setter
@Getter
public class Sku extends BaseEntity {
    @Id
    private Long id;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Boolean online;
    private String img;
    private String title;
    private Long spuId;
//    @Convert(converter = MapAndJson.class)
//    private Map<String, Object> test;
//    @Convert(converter = ListAndJson.class)
//    private List<Object> specs;
    private String specs;
    private String code;
    private Long stock;
    private Long categoryId;
    private Long rootCategoryId;

    public List<Spec> getSpecs() {
        if (specs == null) {
            return Collections.emptyList();
        }
        return GenericAndJson.jsonToObject(specs, new TypeReference<List<Spec>>() {});
    }

    public void setSpecs(String specs) {
        if (specs.isEmpty()) {
            return;
        }
        this.specs = GenericAndJson.objectToJson(specs);
    }

    public BigDecimal getActualPrice() {
        if (discountPrice == null) {
            return price;
        }
        return discountPrice;
    }
}
