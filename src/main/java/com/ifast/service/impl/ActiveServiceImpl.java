package com.ifast.service.impl;

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
}
