package com.ifast.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.api.service.ImgService;
import com.ifast.api.util.JWTUtil;
import com.ifast.common.config.IFastConfig;
import com.ifast.common.utils.DateUtils;
import com.ifast.common.utils.GenUtils;
import com.ifast.domain.ApiUserDO;
import com.ifast.domain.UnitDO;
import com.ifast.oss.sdk.QiNiuOSSService;
import com.ifast.service.UnitService;
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
import sun.tools.jconsole.Plotter;

import java.util.Date;
import java.util.List;
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
    private ImgService imgService;

    @Autowired
    private QiNiuOSSService qiNiuOSS;

    @Autowired
    private UnitService unitService;

    /**
     * 查询产品
     *
     * @param token
     * @return
     */
    @Override
    public List<ProductDO> getProductByToken(String token,String userId) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        if (StringUtils.isBlank(userId)){
            userId = JWTUtil.getUserId(token);
            if (userId == null || "".equals(userId)) {
                return null;
            }
        }
//
//        ApiUserDO userDO = userService.queryById(userId);
//        if(userDO.getUnitDO()==null){
//            return null;
//        }
        UnitDO unitDO=new UnitDO();
        unitDO.setUserId(Long.parseLong(userId));
        unitDO= unitService.selectOne(new EntityWrapper<>(unitDO));
      if (unitDO!=null){
          ProductDO productDO = new ProductDO();
          productDO.setUnitId(unitDO.getId());
          List<ProductDO> list = this.selectList(new EntityWrapper<>(productDO));
          for (ProductDO productDO1 : list) {
              Wrapper<ImgDO> wrapper = new EntityWrapper<>();
              wrapper.eq("parentId", productDO1.getId());
              List<ImgDO> imgs = this.imgService.selectList(wrapper);
              productDO1.setImgs(imgs);
          }
          return list;
      }
        return null;
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
        String newFileName = qiNiuOSS.fileUp(file, conf.getString("file") , UUID.randomUUID().toString().trim().replaceAll("-", ""));

        return "/common"  +"/"+ newFileName ;
    }
}
