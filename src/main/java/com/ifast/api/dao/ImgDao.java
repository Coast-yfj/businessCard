package com.ifast.api.dao;

import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.common.base.BaseDao;

/**
 * <pre>
 * </pre>
 * <small> 2018年4月28日 | Aron</small>
 */
public interface ImgDao extends BaseDao<ImgDO> {

    void addImg(ImgDO imgDO);
}
