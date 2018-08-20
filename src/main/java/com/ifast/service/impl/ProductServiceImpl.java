package com.ifast.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ifast.api.util.JWTUtil;
import com.ifast.domain.ApiUserDO;
import com.ifast.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifast.dao.ProductDao;
import com.ifast.domain.ProductDO;
import com.ifast.service.ProductService;
import com.ifast.common.base.CoreServiceImpl;

/**
 * <pre>
 * @author
 * </pre>
 * <small> 2018-08-19 19:10:02 | Aron</small>
 */
@Service
public class ProductServiceImpl extends CoreServiceImpl<ProductDao, ProductDO> implements ProductService {

    @Autowired
    private UserService userService;

    /**
     * 查询产品
     *
     * @param token
     * @return
     */
    @Override
    public ProductDO getProductByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String userId = JWTUtil.getUserId(token);
        if (userId == null || "".equals(userId)) {
            return null;
        }
        ApiUserDO userDO = userService.queryById(userId);
        ProductDO productDO = new ProductDO();
        productDO.setUnitId(userDO.getUnitDO().getId());
        return this.selectOne(new EntityWrapper<>(productDO));
    }


}
