package com.ifast.api.service;

import com.ifast.api.pojo.domain.ApiQunDO;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.common.base.CoreService;

import java.util.List;

/**
 * <pre>
 * </pre>
 * <small> 2018年4月27日 | Aron</small>
 */
public interface ApiQunService extends CoreService<ApiQunDO> {

    List<ApiQunDO> queryRenyuan(String openGId);
}
