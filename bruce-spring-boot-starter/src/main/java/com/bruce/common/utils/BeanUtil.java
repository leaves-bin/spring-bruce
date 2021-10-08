package com.bruce.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.common.vo.RespPage;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * BeanUtil
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/10/6
 */
public class BeanUtil {

    /**
     * 拷贝
     *
     * @return
     */
    public static <T> T copy(Object source, Class<T> target) {
        try {
            if (Objects.isNull(source)) {
                return null;
            }
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param list
     * @param target
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List list, Class<T> target) {
        try {
            if (list == null || list.size() == 0) {
                return null;
            }
            return (List<T>) list.parallelStream().map(item -> copy(item, target)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param data
     * @param target
     * @param <T>
     * @return
     */
    public static <T> RespPage<T> copyPage(Page data, Class<T> target) {
        RespPage respPage = new RespPage();
        respPage.setTotal(data.getTotal());
        respPage.setPageSize(data.getSize());
        respPage.setCurrent(data.getCurrent());
        respPage.setTotalPages(data.getPages());
        respPage.setData(BeanUtil.copyList(data.getRecords(), target));
        return respPage;
    }

}
