package com.ifast.service;

import com.ifast.api.pojo.vo.TokenVO;
import com.ifast.domain.ApiUserDO;
import com.ifast.common.base.CoreService;

import java.io.IOException;

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

     /**
      * 登陆token
      * @param code
      * @return
      * @throws IOException
      */
     String getToken (String code) throws IOException;

     /**
      * 验证token
      * @param token
      * @return
      */
     boolean verifyToken(String token);

     /**
      * 关联查询
      * @param id
      * @return
      */
     ApiUserDO queryById(String id);


}
