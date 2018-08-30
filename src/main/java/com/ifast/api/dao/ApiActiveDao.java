package com.ifast.api.dao;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.common.base.BaseDao;

import java.util.List;

/**
 * <pre>
 * </pre>
 * <small> 2018年4月28日 | Aron</small>
 */
public interface ApiActiveDao extends BaseDao<ActiveDO> {

    List<ActiveDO>  queryActive(Pagination page, ActiveDO activeDO);
}
