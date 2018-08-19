package com.ifast.service;

import com.ifast.domain.ApiUserDO;
import com.ifast.common.base.CoreService;

/**
 * 
 * <pre>
 * @author
 * </pre>
 * <small> 2018-08-19 19:10:03 | Aron</small>
 */
public interface UserService extends CoreService<ApiUserDO> {
     /**
      * 查询用户
      * @param token
      * @return
      */
     ApiUserDO getUserByToken(String token);
    
}
