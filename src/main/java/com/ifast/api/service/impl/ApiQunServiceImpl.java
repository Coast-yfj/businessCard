package com.ifast.api.service.impl;

import com.ifast.api.dao.ApiQunDao;
import com.ifast.api.dao.ImgDao;
import com.ifast.api.pojo.domain.ApiQunDO;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.api.service.ApiQunService;
import com.ifast.api.service.ImgService;
import com.ifast.common.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 * </pre>
 * 
 * <small> 2018年4月27日 | Aron</small>
 */
@Service
public class ApiQunServiceImpl extends CoreServiceImpl<ApiQunDao, ApiQunDO> implements ApiQunService {
    @Override
    public List<ApiQunDO> queryRenyuan(String openGId) {
        return this.baseMapper.queryRenyuan(openGId);
    }
}
