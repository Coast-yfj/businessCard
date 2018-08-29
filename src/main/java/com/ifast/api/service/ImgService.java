package com.ifast.api.service;

import com.ifast.api.dao.ActiveUserDao;
import com.ifast.api.dao.ImgDao;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.common.base.CoreService;

/**
 * <pre>
 * </pre>
 * <small> 2018年4月27日 | Aron</small>
 */
public interface ImgService extends CoreService<ImgDO> {

    void addImg(ImgDO imgDO);
}
