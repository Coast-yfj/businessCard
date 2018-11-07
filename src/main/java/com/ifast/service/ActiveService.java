package com.ifast.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.common.base.CoreService;

import java.util.List;

/**
 * 
 * <pre>
 * 
 * </pre>
 * <small> 2018-08-29 22:44:49 | Aron</small>
 */
public interface ActiveService extends CoreService<ActiveDO> {
    List<String> sheng( );
    List<String> shi( );
    List<String> qu( );

    /**
     * 分页查询
     * @param page
     * @param activeDO
     * @return
     */
    Page<ActiveDO> queryByPage(Page page, ActiveDO activeDO);
}
