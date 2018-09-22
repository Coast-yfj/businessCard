package com.ifast.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ifast.domain.ApiUserDO;
import com.ifast.common.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 
 * <pre>
 * @author
 * </pre>
 * <small> 2018-08-19 19:10:03 | Aron</small>
 */
public interface ApiUserDao extends BaseDao<ApiUserDO> {
    /**
     * id查询用户信息
     * @param id
     * @return
     */
    ApiUserDO queryById(String id);

    /**
     * 分页查询用户信息
     * @param page
     * @param apiUserDO
     * @return
     */
    List<ApiUserDO> queryByUserDo(Pagination page, ApiUserDO apiUserDO);

    /**
     * 查询名片夹
     * @param id
     * @return
     */
    List<ApiUserDO> queryByIds(@Param("id") String id,@Param("attention") String attention);
}
