package com.ifast.api.dao;

import com.ifast.api.pojo.domain.ApiQunDO;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.common.base.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <pre>
 * </pre>
 * <small> 2018年4月28日 | Aron</small>
 */
public interface ApiQunDao extends BaseDao<ApiQunDO> {
    List<ApiQunDO> queryRenyuan(@Param("openGId") String openGId,@Param("limit")String limit);
}
