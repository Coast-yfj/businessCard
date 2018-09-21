package com.ifast.service;

import com.ifast.domain.ProductDO;
import com.ifast.common.base.CoreService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    List<ProductDO> getProductByToken(String token,String userId);
    /**
     * <pre>
     * 上传文件，默认路径为
     *      http:// + 七牛默认分配的域名 + / + 项目名 + / + 日期 + / + 文件名 + "-" + 时间戳 + "." + 后缀
     *      如：http://p6r7ke2jc.bkt.clouddn.com/ifast/20180406/cat001-123412412431.jpeg
     * </pre>
     *
     * <small> 2018年4月6日 | Aron</small>
     *
     * @param file 简单文件名，带后缀，如：mycat.png
     * @return
     */
    String upload(MultipartFile file);
    
}
