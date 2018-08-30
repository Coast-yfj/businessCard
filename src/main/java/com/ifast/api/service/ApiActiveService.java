package com.ifast.api.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.common.base.CoreService;

/**
 * <pre>
 * </pre>
 * <small> 2018年4月27日 | Aron</small>
 */
public interface ApiActiveService extends CoreService<ActiveDO> {

    /**
     * 查询活动列表
     * @param activeDO
     * @return
     */
    Page<ActiveDO> active(ActiveDO activeDO);


}
