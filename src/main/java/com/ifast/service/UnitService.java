package com.ifast.service;

import com.ifast.domain.UnitDO;
import com.ifast.common.base.CoreService;

/**
 * 
 * <pre>
 * @author
 * </pre>
 * <small> 2018-08-19 19:10:02 | Aron</small>
 */
public interface UnitService extends CoreService<UnitDO> {
    /**
     * 根据用户token查询公司信息
     * @param token
     * @return
     */
    UnitDO getUnitByToken(String token,String userId);
    
}
