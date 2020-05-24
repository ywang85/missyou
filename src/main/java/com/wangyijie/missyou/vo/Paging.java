package com.wangyijie.missyou.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class Paging<T> {
    private Long total; // 总数量
    private Integer count; // 当前请求数据总共多少条
    private Integer page; // 页码
    private Integer totalPage; // 总共多少页
    private List<T> items;

    public Paging(Page<T> pageT) {
        initPageParameters(pageT);
        items = pageT.getContent();
    }

    void initPageParameters(Page<T> pageT) {
        total = pageT.getTotalElements(); // 总共多少条
        count = pageT.getSize(); // 当前请求数据总共多少条
        page = pageT.getNumber(); // 当前第几页
        totalPage = pageT.getTotalPages(); // 总共多少页
    }
}
