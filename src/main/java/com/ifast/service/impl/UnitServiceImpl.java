package com.ifast.service.impl;

import com.ifast.api.util.JWTUtil;
import com.ifast.domain.ApiUserDO;
import com.ifast.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifast.dao.UnitDao;
import com.ifast.domain.UnitDO;
import com.ifast.service.UnitService;
import com.ifast.common.base.CoreServiceImpl;

/**
 * <pre>
 *
 * </pre>
 * <small> 2018-08-19 19:10:02 | Aron</small>
 */
@Service
public class UnitServiceImpl extends CoreServiceImpl<UnitDao, UnitDO> implements UnitService {

    @Autowired
    private UserService userService;

    /**
     * 根据用户token查询公司信息
     *
     * @param token
     * @return
     */
    @Override
    public UnitDO getUnitByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String userId = JWTUtil.getUserId(token);
        if (userId == null || "".equals(userId)) {
            return null;
        }
       ApiUserDO userDO = userService.selectById(userId);
        return this.selectById(userDO.getUnitId());
    }
}
