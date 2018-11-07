package com.ifast.dao;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.common.base.BaseDao;

import java.util.List;

/**
 * 
 * <pre>
 * 
 * </pre>
 * <small> 2018-08-29 22:44:49 | Aron</small>
 */
public interface ActiveDao extends BaseDao<ActiveDO> {
    List<String> sheng();
    List<String> shi();
    List<String> qu();

    /**
     * 分页查询
     * @param page
     * @param activeDO
     * @return
     */
    List<ActiveDO> queryByPage(Pagination page, ActiveDO activeDO);

}
