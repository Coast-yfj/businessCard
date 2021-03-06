package com.ifast.api.service.impl;

import com.ifast.api.dao.ImgDao;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.api.service.ImgService;
import com.ifast.common.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * </pre>
 * 
 * <small> 2018年4月27日 | Aron</small>
 */
@Service
public class ImgServiceImpl extends CoreServiceImpl<ImgDao, ImgDO> implements ImgService {
    @Override
    public void addImg(ImgDO imgDO) {
        this.baseMapper.addImg(imgDO);
    }
}
