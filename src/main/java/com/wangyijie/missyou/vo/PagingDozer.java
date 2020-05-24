package com.wangyijie.missyou.vo;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PagingDozer<T, K> extends Paging {
    public PagingDozer(Page<T> pageT, Class<K> classK) {
        initPageParameters(pageT);
        List<T> tList = pageT.getContent();
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        List<K> voList = new ArrayList<>(); // 目标
        tList.forEach(t-> {
            K vo = mapper.map(t, classK);
            voList.add(vo);
        });
//        for (T t : tList) {
//            K vo = mapper.map(t, classK);
//            voList.add(vo);
//        }
        setItems(voList);
    }
}
