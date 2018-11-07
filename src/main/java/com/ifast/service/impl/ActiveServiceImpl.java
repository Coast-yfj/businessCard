package com.ifast.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.common.base.CoreServiceImpl;
import com.ifast.dao.ActiveDao;
import com.ifast.service.ActiveService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * <pre>
 * 
 * </pre>
 * <small> 2018-08-29 22:44:49 | Aron</small>
 */
@Service
public class ActiveServiceImpl extends CoreServiceImpl<ActiveDao, ActiveDO> implements ActiveService {

    @Override
    public List<String> sheng( ) {
        return this.baseMapper.sheng();
    }

    @Override
    public List<String> shi( ) {
        return baseMapper.shi();
    }

    @Override
    public List<String> qu( ) {
        return baseMapper.qu();
    }

    /**
     * 分页查询
     *
     * @param page
     * @param activeDO
     * @return
     */
    @Override
    public Page<ActiveDO> queryByPage(Page page, ActiveDO activeDO) {
        return page.setRecords(baseMapper.queryByPage(page,activeDO));
    }
}
