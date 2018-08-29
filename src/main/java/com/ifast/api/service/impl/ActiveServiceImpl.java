package com.ifast.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.api.config.JWTConfig;
import com.ifast.api.dao.ActiveDao;
import com.ifast.api.dao.AppUserDao;
import com.ifast.api.exception.IFastApiException;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.api.pojo.domain.AppUserDO;
import com.ifast.api.pojo.vo.TokenVO;
import com.ifast.api.service.ActiveService;
import com.ifast.api.service.UserService;
import com.ifast.api.util.JWTUtil;
import com.ifast.common.base.CoreServiceImpl;
import com.ifast.common.config.CacheConfiguration;
import com.ifast.common.config.IFastConfig;
import com.ifast.common.type.EnumErrorCode;
import com.ifast.common.utils.SpringContextHolder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * <pre>
 * </pre>
 * 
 * <small> 2018年4月27日 | Aron</small>
 */
@Service
public class ActiveServiceImpl extends CoreServiceImpl<ActiveDao, ActiveDO> implements ActiveService {
    @Override
    public Page<ActiveDO> active(ActiveDO activeDO) {
        Page<ActiveDO> page = new Page<>(activeDO.getPageNo(),activeDO.getPageSize());
        return page.setRecords(this.baseMapper.active(page, activeDO));
    }
}
