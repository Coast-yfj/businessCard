package com.ifast.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ifast.api.util.JWTUtil;
import com.ifast.common.config.IFastConfig;
import com.ifast.common.utils.DateUtils;
import com.ifast.common.utils.GenUtils;
import com.ifast.domain.ApiUserDO;
import com.ifast.oss.sdk.QiNiuOSSService;
import com.ifast.service.UserService;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifast.dao.ProductDao;
import com.ifast.domain.ProductDO;
import com.ifast.service.ProductService;
import com.ifast.common.base.CoreServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

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
    @Autowired
    private IFastConfig ifastConfig;

    @Autowired
    private QiNiuOSSService qiNiuOSS;

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

    /**
     * <pre>
     * 上传文件，默认路径为
     *      http:// + 七牛默认分配的域名 + / + 项目名 + / + 日期 + / + 文件名 + "-" + 时间戳 + "." + 后缀
     *      如：http://p6r7ke2jc.bkt.clouddn.com/ifast/20180406/cat001-123412412431.jpeg
     * </pre>
     *
     * <small> 2018年4月6日 | Aron</small>
     *
     * @param file 文件
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        Configuration conf = GenUtils.getConfigFile();
        String savePath = "/img";
        String newFileName = qiNiuOSS.fileUp(file, conf.getString("file") + savePath, UUID.randomUUID().toString().trim().replaceAll("-", ""));

        return "/common/" + savePath +"/"+ newFileName ;
    }
}
