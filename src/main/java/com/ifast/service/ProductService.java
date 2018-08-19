package com.ifast.service;

import com.ifast.domain.ProductDO;
import com.ifast.common.base.CoreService;

/**
 * 
 * <pre>
 * @author
 * </pre>
 * <small> 2018-08-19 19:10:02 | Aron</small>
 */
public interface ProductService extends CoreService<ProductDO> {

    /**
     * 查询产品
     * @param token
     * @return
     */
    ProductDO getProductByToken(String token);
    
}
