package com.bruce.search.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bruce.search.biz.domain.PageDetail;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 站内消息提示 Mapper 接口
 * </p>
 *
 * @author yongbin
 * @since 2022-01-06
 */
public interface PageDetailMapper extends BaseMapper<PageDetail> {

    List<PageDetail> selectPages(@Param("idList") Set<Long> idList);

}
