package com.ifast.api.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.ifast.api.pojo.domain.ApiQunDO;
import com.ifast.api.pojo.domain.ApiSuijishuDO;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.api.service.ApiQunService;
import com.ifast.api.service.ApiSuijishuService;
import com.ifast.api.service.ImgService;
import com.ifast.api.util.JWTUtil;
import com.ifast.api.util.SignUtil;
import com.ifast.common.utils.Result;
import com.ifast.domain.*;
import com.ifast.service.*;
import com.ifast.sys.domain.UserDO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yfj
 */
@RestController
@RequestMapping("/api/businessCard/")
public class ApiBusinessCard {

    @Resource(name = "apiService")
    private UserService userService;

    @Autowired
    private ImgService imgService;
    @Autowired
    private ApiSuijishuService apiSuijishuService;
    @Autowired
    private UnitService unitService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AttentionService attentionService;

    @Autowired
    private AboutService aboutService;
    @Autowired
    private ApiQunService apiQunService;

    @PostMapping("login")
    @ApiOperation("api登录")
    public Result<?> login(String code, String iv, String encryptedData) {
        String str = "";
        try {
            str = userService.getToken(code, iv, encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
        if (str == "") {
            return Result.fail();
        }
        return Result.ok(str);
    }

    @GetMapping("/queryUser")
    @ApiOperation("查询用户信息")
    public Result<?> queryUser(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, String userId) {
        if (StringUtils.isNotBlank(userId)) {
            ApiUserDO userDO = userService.queryById(userId);
            if (userDO == null) {
                Result result = new Result();
                result.setMsg("未查到此人信息");
                result.setCode(-1);
                return result;
            }
            String tid = JWTUtil.getUserId(token);
            Wrapper<AttentionDO> wrapper = new EntityWrapper<>();
            wrapper.eq("mid", tid);
            wrapper.eq("tid", userId);
            AttentionDO attentionDO = this.attentionService.selectOne(wrapper);
            if (attentionDO != null) {
                userDO.setAttention(attentionDO.getAttention());
                userDO.setInCard("1");
            } else {
                //0：未关注，0：未在名片夹
                userDO.setInCard("0");
                userDO.setAttention("0");
            }
            return Result.ok(userDO);
        }
        return Result.ok(userService.getUserByToken(token));
    }

    @GetMapping("/queryUnit")
    @ApiOperation("查询公司信息")
    public Result<?> queryUnit(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, String userId) {
        return Result.ok(unitService.getUnitByToken(token, userId));
    }

    @RequestMapping("/queryProduct")
    @ApiOperation("查询产品")
    public Result<?> queryProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, String userId) {
        return Result.ok(productService.getProductByToken(token, userId));
    }

    @PostMapping("/productDetail")
    @ApiOperation("查询产品详情")
    public Result<?> detailProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , String id) {
        ProductDO productDO = this.productService.selectById(id);
        if(productDO==null){
            return Result.ok("查询不到产品");
        }
        Wrapper<ImgDO> wrapper = new EntityWrapper<>();
        wrapper.eq("parentId", productDO.getId());
        List<ImgDO> list = this.imgService.selectList(wrapper);
        List urlList=new ArrayList<String>(list.size());
        for (ImgDO img:list
             ) {
            urlList.add(img.getUrl());
        }
        productDO.setImgs(urlList);
        return Result.ok(productDO);

    }

    @PostMapping("saveUser")
    @ApiOperation("保存个人信息")
    public Result<?> saveUser(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, ApiUserDO apiUserDO) {
        String userId = JWTUtil.getUserId(token);
        apiUserDO.setId(Long.parseLong(userId));
        userService.insertOrUpdate(apiUserDO);
        return Result.ok();
    }

    @PostMapping("/saveUnit")
    @ApiOperation("保存公司信息")
    public Result<?> saveUnit(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, UnitDO unitDO) {
        ApiUserDO userDO = userService.getUserByToken(token);
        unitDO.setUserId(userDO.getId());
        if (userDO.getUnitDO() != null) {
            unitDO.setId(userDO.getUnitDO().getId());
        }
        unitService.insertOrUpdate(unitDO);
        return Result.ok(unitDO);
    }

    @PostMapping("/saveProduct")
    @ApiOperation("保存产品信息")
    @Transactional
    public Result<?> saveProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, ProductDO productDO) {
        /*String url=productService.upload(file);
        productDO.setPath(url);*/

        //前端要求根据用户id
        UnitDO unitDO = this.unitService.getUnitByToken(token, null);
        if (unitDO == null) {
            return Result.build(1, "用户信息为空");
        }
        productDO.setUnitId(unitDO.getId());
        productService.insertOrUpdate(productDO);
        Long parentId = productDO.getId();
        // List<Long> imgids = productDO.getImgIds();
        List<ImgDO> imgs = Lists.newArrayList();
        List<ImgDO> imgDOList = imgService.selectList(new EntityWrapper<>(new ImgDO()).in("url", productDO.getImgIds()));
        if (imgDOList != null && imgDOList.size() > 0) {
            for (ImgDO imgDO : imgDOList) {
                imgDO.setParentId(parentId);
                imgs.add(imgDO);
            }
            this.imgService.updateBatchById(imgs);
        }
        return Result.ok(productDO);
    }


    @PostMapping("/updateUser")
    @ApiOperation("更新用户信息")
    public Result<?> updateUser(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, ApiUserDO apiUserDO) {
        String userId = JWTUtil.getUserId(token);
        apiUserDO.setId(Long.parseLong(userId));
        userService.updateById(apiUserDO);
        return Result.ok();
    }

    @PostMapping("/updateUnit")
    @ApiOperation("更新公司信息")
    public Result<?> updateUnit(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, UnitDO unitDO) {
        ApiUserDO userDO = userService.getUserByToken(token);
        unitDO.setUserId(userDO.getId());
        unitService.insertOrUpdate(unitDO);
        return Result.ok(unitDO);
    }

    @PostMapping("/updateProduct")
    @ApiOperation("更新产品信息")
    @Transactional
    public Result<?> updateProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, ProductDO productDO) {
        productService.updateById(productDO);
        Long parentId = productDO.getId();
        List<String > imgids = productDO.getImgIds();
        List<ImgDO> imgs = Lists.newArrayList();
        imgService.delete(new EntityWrapper<>(new ImgDO()).eq("parentId",parentId));
        if (imgids != null && imgids.size() > 0) {
            for (String  url : imgids) {
                ImgDO imgDO = new ImgDO();
                imgDO.setParentId(parentId);
                imgDO.setUrl(url);
                imgs.add(imgDO);
            }
            this.imgService.insertBatch(imgs);
        }
//        return this.detailProduct(token,parentId.toString());
        return Result.ok();
    }

    @PostMapping("/delProduct")
    @ApiOperation("删除产品信息")
    @Transactional
    public Result<?> delProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, String id) {
        productService.deleteById(Long.parseLong(id));
        Wrapper<ImgDO> wrapper = new EntityWrapper<>();
        wrapper.eq("parentId", id);
        this.imgService.delete(wrapper);
        return Result.ok();
    }

    @PostMapping("/joinCard")
    @ApiOperation("加入名片夹")
    public Result<?> joinCard(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, String id) {
        ApiUserDO userDO = userService.getUserByToken(token);
        Wrapper<AttentionDO> wrapper = new EntityWrapper<>();
        wrapper.eq("mid", userDO.getId()).eq("tid", id);
        AttentionDO attentionDO = this.attentionService.selectOne(wrapper);
        if (attentionDO != null) {
            this.attentionService.delete(wrapper);
            return Result.ok();
        }

        attentionDO = new AttentionDO();
        attentionDO.setMid(userDO.getId());
        attentionDO.setTid(Long.getLong(id));
        attentionDO.setAttention("0");
        attentionService.insert(attentionDO);
        return Result.ok();
    }


    @PostMapping("/attention")
    @ApiOperation("关注")
    public Result<?> attention(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, String id) {
        ApiUserDO userDO = userService.getUserByToken(token);
        Wrapper<AttentionDO> wrapper = new EntityWrapper<>();
        wrapper.eq("mid", userDO.getId()).eq("tid", id);
        AttentionDO attentionDO = this.attentionService.selectOne(wrapper);
        if (attentionDO == null) {
            return Result.fail();
        }
        if (Objects.equals(attentionDO.getAttention(), "0")) {
            attentionDO.setAttention("1");
        } else {
            attentionDO.setAttention("0");
        }
        attentionService.updateById(attentionDO);
        return Result.ok();
    }

    @RequestMapping("/queryCardHolder")
    @ApiOperation("查询名片夹信息")
    public Result<?> queryCardHolder(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , String attention,String name) {
        String userId = JWTUtil.getUserId(token);
        return Result.ok(userService.queryByIds(userId, attention,name));
    }

    @GetMapping("/queryAbout")
    @ApiOperation("关于我们信息")
    public Result<?> queryAbout(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token) {

        return Result.ok(aboutService.selectOne(new EntityWrapper<>(new AboutDO())));
    }

    @PostMapping("/sign")
    @ApiOperation("签名")
    Result<?> sign(long appId, String secretId, String secretKey, String bucketName,
                   long expired) {
        try {
            return Result.ok(SignUtil.appSign(appId, secretId, secretKey, bucketName, expired));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }

    @PostMapping("/saveSuijishu")
    @ApiOperation("保存随机数")
    Result<?> saveSuijishu(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token,
                           String suijishu) {
        if (StringUtils.isBlank(suijishu)) {
            Result result = new Result();
            result.setCode(1);
            result.setMsg("随机数不能为空");
            return result;
        }
        String userId = JWTUtil.getUserId(token);
        ApiSuijishuDO suijishuDO = new ApiSuijishuDO();
        suijishuDO.setSuijishu(suijishu);
        suijishuDO.setUserId(Long.getLong(userId));
        this.apiSuijishuService.insert(suijishuDO);
        return Result.ok();
    }

    @PostMapping("/isInCard")
    @ApiOperation("查询是否在列表中")
    Result<?> isInCard(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , String userId) {
        String tid = JWTUtil.getUserId(token);
        Wrapper<AttentionDO> wrapper = new EntityWrapper<>();
        wrapper.eq("mid", userId);
        wrapper.eq("tid", tid);
        AttentionDO attentionDO = this.attentionService.selectOne(wrapper);
        return Result.ok(attentionDO);
    }

    @PostMapping("/joinQun")
    @ApiOperation("加入群")
    Result<?> joinQun(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , String suijishu, String iv, String encryptedData) throws Exception {
        String userId = JWTUtil.getUserId(token);
        ApiUserDO userDO = this.userService.selectById(userId);
        Map<String, String> userInfo = this.userService.getUserInfo(encryptedData, userDO.getSession_key(), iv);
        if(userInfo.keySet().contains("openGId")){
            String openGId = userInfo.get("openGId");
            Wrapper<ApiSuijishuDO> wrapper = new EntityWrapper<>();
            wrapper.eq("suijiashu", suijishu);
            ApiSuijishuDO suijishuDO = this.apiSuijishuService.selectOne(wrapper);
            ApiQunDO qunDO = new ApiQunDO();
            qunDO.setOpenGId(openGId);
            qunDO.setUserId(Long.getLong(userId));
            this.apiQunService.insert(qunDO);
            qunDO = new ApiQunDO();
            qunDO.setUserId(suijishuDO.getUserId());
            qunDO.setOpenGId(openGId);
            this.apiQunService.insert(qunDO);
            return Result.ok();
        }
        return Result.fail();
    }

    @PostMapping("/queryQun")
    @ApiOperation("查询群成员")
    Result<?> queryQun(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            ,String openGId ,String limit ){
        return Result.ok(this.apiQunService.queryRenyuan(openGId,limit));
    }


}
