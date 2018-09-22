package com.ifast.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.domain.ApiUserDO;
import com.ifast.common.base.CoreService;

import java.io.IOException;
import java.util.List;

/**
 * <pre>
 * @author
 * </pre>
 * <small> 2018-08-19 19:10:03 | Aron</small>
 */
public interface UserService extends CoreService<ApiUserDO> {
    /**
     * 查询用户
     *
     * @param token
     * @return
     */
    ApiUserDO getUserByToken(String token);

    /**
     * 登陆token
     *
     * @param code
     * @param encryptedData
     * @param iv
     * @return
     * @throws Exception
     */
    String getToken(String code,String iv,String encryptedData) throws Exception;

    /**
     * 验证token
     *
     * @param token
     * @return
     */
    boolean verifyToken(String token);

    /**
     * 关联查询
     *
     * @param id
     * @return
     */
    ApiUserDO queryById(String id);

    /**
     * 分页查询用户信息
     *
     * @param page
     * @param userDO
     * @return
     */
    Page<ApiUserDO> queryUserPage(Page page, ApiUserDO userDO);

    /**
     * 查询名片夹
     * @param id
     * @return
     */
    List<ApiUserDO> queryByIds(String id,String attention);


}
