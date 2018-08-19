package com.ifast.service.impl;

import com.ifast.api.util.JWTUtil;
import com.ifast.dao.ApiUserDao;
import com.ifast.domain.ApiUserDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ifast.service.UserService;
import com.ifast.common.base.CoreServiceImpl;

/**
 * 
 * <pre>
 * @author
 * </pre>
 * <small> 2018-08-19 19:10:03 | Aron</small>
 */
@Service(value = "apiService")
public class UserServiceImpl extends CoreServiceImpl<ApiUserDao, ApiUserDO> implements UserService {
    @Override
    public ApiUserDO getUserByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String userId = JWTUtil.getUserId(token);
        if (userId == null || "".equals(userId)) {
            return null;
        }
        return this.selectById(userId);
    }

}
