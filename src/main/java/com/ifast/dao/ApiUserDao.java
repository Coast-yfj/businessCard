package com.ifast.dao;

import com.ifast.domain.ApiUserDO;
import com.ifast.common.base.BaseDao;

/**
 * 
 * <pre>
 * @author
 * </pre>
 * <small> 2018-08-19 19:10:03 | Aron</small>
 */
public interface ApiUserDao extends BaseDao<ApiUserDO> {

    ApiUserDO queryById(String id);

}
