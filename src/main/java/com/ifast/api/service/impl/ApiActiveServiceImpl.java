package com.ifast.api.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.api.dao.ApiActiveDao;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.api.service.ApiActiveService;
import com.ifast.common.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * </pre>
 * 
 * <small> 2018年4月27日 | Aron</small>
 */
@Service
public class ApiActiveServiceImpl extends CoreServiceImpl<ApiActiveDao, ActiveDO> implements ApiActiveService {
    @Override
    public Page<ActiveDO> active(ActiveDO activeDO) {
        Page<ActiveDO> page = new Page<>(activeDO.getPageNo(),activeDO.getPageSize());
        return page.setRecords(this.baseMapper.active(page, activeDO));
    }
}
