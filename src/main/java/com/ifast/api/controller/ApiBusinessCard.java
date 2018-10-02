package com.ifast.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ifast.api.pojo.domain.ApiQunDO;
import com.ifast.api.pojo.domain.ApiSuijishuDO;
import com.ifast.api.pojo.domain.ImgDO;
import com.ifast.api.service.ApiQunService;
import com.ifast.api.service.ApiSuijishuService;
import com.ifast.api.service.ImgService;
import com.ifast.api.util.JWTUtil;
import com.ifast.api.util.SignUtil;
import com.ifast.common.utils.GenUtils;
import com.ifast.common.utils.Result;
import com.ifast.domain.*;
import com.ifast.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

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
        if (productDO == null) {
            return Result.ok("查询不到产品");
        }
        Wrapper<ImgDO> wrapper = new EntityWrapper<>();
        wrapper.eq("parentId", productDO.getId());
        List<ImgDO> list = this.imgService.selectList(wrapper);
        List urlList = new ArrayList<String>(list.size());
        for (ImgDO img : list
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
        ApiUserDO userDO = userService.getUserByToken(token);
        UnitDO unitDO = apiUserDO.getUnitDO();
        if (unitDO != null) {
            unitDO.setUserId(userDO.getId());
            if (userDO.getUnitDO() != null) {
                unitDO.setId(userDO.getUnitDO().getId());
            }
            unitService.insertOrUpdate(unitDO);
        }
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
        List<String> imgids = productDO.getImgIds();
        List<ImgDO> imgs = Lists.newArrayList();
        imgService.delete(new EntityWrapper<>(new ImgDO()).eq("parentId", parentId));
        if (imgids != null && imgids.size() > 0) {
            for (String url : imgids) {
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
    public Result<?> joinCard(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , String id) {
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
        attentionDO.setTid(Long.parseLong(id));
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
            , String attention, String name) {
        String userId = JWTUtil.getUserId(token);
        return Result.ok(userService.queryByIds(userId, attention, name));
    }

    @GetMapping("/queryAbout")
    @ApiOperation("关于我们信息")
    public Result<?> queryAbout(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token) {

        return Result.ok(aboutService.selectOne(new EntityWrapper<>(new AboutDO())));
    }

    @PostMapping("/sign")
    @ApiOperation("签名")
    Result<?> sign() {
        try {
            return Result.ok(SignUtil.appSign(111, null, null, null, 111111111));
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
        suijishuDO.setUserId(Long.parseLong(userId));
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
    @Transactional
    Result<?> joinQun(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , String suijishu, String iv, String encryptedData, String code) throws Exception {
        System.out.println("***************************************");
        System.out.println("iv====="+iv);
        System.out.println("encryptedData===="+encryptedData);
        System.out.println("suijishu===="+suijishu);
        System.out.println("code===="+code);
        System.out.println(token);
        System.out.println("***************************************");
        String userId = JWTUtil.getUserId(token);
        try {
            String str = userService.getToken(code, iv, encryptedData);
            //userId = str;
        } catch (Exception e) {
            e.printStackTrace();
//            return Result.fail();
        }
        ApiUserDO userDO = this.userService.selectById(userId);
        Map<String, String> userInfo = this.userService.getUserInfo(encryptedData, userDO.getSession_key(), iv);
        /*for (String string : userInfo.keySet()) {
            System.out.println(string);
        }*/
        if (userInfo.keySet().contains("openGId")) {
            String openGId = userInfo.get("openGId");
            Wrapper<ApiSuijishuDO> wrapper = new EntityWrapper<>();
            wrapper.eq("suijishu", suijishu);
            ApiSuijishuDO suijishuDO = this.apiSuijishuService.selectOne(wrapper);
            System.out.println(suijishuDO==null);
            ApiQunDO qunDO = new ApiQunDO();
            qunDO.setOpenGId(openGId);
            qunDO.setUserId(userId);
            this.apiQunService.insert(qunDO);
            Wrapper<ApiQunDO> qunDOWrapper = new EntityWrapper<>();
            qunDOWrapper.eq("userId", suijishuDO.getUserId()).eq("openGId", openGId);
            ApiQunDO qunzhu = this.apiQunService.selectOne(qunDOWrapper);
            if (qunzhu == null) {
                qunDO = new ApiQunDO();
                qunDO.setUserId(String.valueOf(suijishuDO.getUserId()));
                qunDO.setOpenGId(openGId);
                this.apiQunService.insert(qunDO);
            }
            return Result.ok();
        }
        return Result.fail();
    }

    @PostMapping("/queryQun")
    @ApiOperation("查询群成员")
    Result<?> queryQun(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , String openGId, String limit) {
        return Result.ok(this.apiQunService.queryRenyuan(openGId, limit));
    }

    @PostMapping("/qun")
    @ApiOperation("查询群")
    Result<?> qun(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
    ) {
        String userId = String.valueOf(this.userService.getUserByToken(token).getId());
        Wrapper<ApiQunDO> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId);
        List<ApiQunDO> apiQunDOList = this.apiQunService.selectList(wrapper);
//        List<String> list = new ArrayList<>();
        Map<String, List<ApiQunDO>> map = Maps.newHashMap();
        for (ApiQunDO qunDO : apiQunDOList) {
//            list.add(qunDO.getOpenGId());
            List<ApiQunDO> list1 = this.apiQunService.queryRenyuan(qunDO.getOpenGId(), null);
            map.put(qunDO.getOpenGId(), list1);
        }
        return Result.ok(map);
    }

    @PostMapping("/tuiqun")
    @ApiOperation("退出群")
    Result<?> tuiqun(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
    ,String openGId){
        String userId = String.valueOf(this.userService.getUserByToken(token).getId());
        Wrapper<ApiQunDO> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId).eq("openGId", openGId);
        ApiQunDO qunDO = this.apiQunService.selectOne(wrapper);
        if (qunDO != null) {
            this.apiQunService.delete(wrapper);
        }
        return Result.ok();
    }

    /*
     * 获取二维码
     * 这里的 post 方法 为 json post【重点】
     */
    @RequestMapping("/getCode")
    @ApiOperation("图片")
    public Result getCodeM(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token
            , String scene) throws Exception {

        String userId = String.valueOf(this.userService.getUserByToken(token).getId());
        String imei = "867186032552993";
        String page = "page/msg_waist/msg_waist";
        String access_token = getToken();   // 得到token

        Map<String, Object> params = new HashMap<>();
        params.put("scene", scene);  //参数
        //  params.put("page", "pages/carddetails/carddetails?uid="+userId); //位置
        params.put("width", 430);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + access_token);  // 接口
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        String body = JSON.toJSONString(params);           //必须是json模式的 post
        StringEntity entity;
        entity = new StringEntity(body);
        entity.setContentType("image/png");

        httpPost.setEntity(entity);
        HttpResponse response;

        response = httpClient.execute(httpPost);
        InputStream inputStream = response.getEntity().getContent();
        System.out.println(inputStream.available());
        String name = UUID.randomUUID().toString().trim().replaceAll("-", "") + ".png";
        Configuration conf = GenUtils.getConfigFile();
        saveToImgByInputStream(inputStream, conf.getString("file"), name);  //保存图片
        return Result.ok("/common/" + name);
    }

    /*
     * 获取 token
     * 普通的 get 可获 token
     */
    public String getToken() {
        try {
            HttpGet httpGet = new HttpGet(
                    "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                            + "wxb38247846e8fda09" + "&secret="
                            + "7057ec8449d6c9eaad85c11fb01e18d1");
            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse res = httpClient.execute(httpGet);
            HttpEntity entity = res.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println(result);
            JSONObject json = JSONObject.parseObject(result);

            if (json.getString("access_token") != null || json.getString("access_token") != "") {
                return json.getString("access_token");
            } else {
                return null;
            }

        } catch (Exception e) {
//            log.error("# 获取 token 出错... e:" + e);
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        System.out.println();
        try {
//            getCodeM();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将二进制转换成文件保存
     *
     * @param instreams 二进制流
     * @param imgPath   图片的保存路径
     * @param imgName   图片的名称
     * @return 1：保存正常
     * 0：保存失败
     */
    public static int saveToImgByInputStream(InputStream instreams, String imgPath, String imgName) {
        int stateInt = 1;
        if (instreams != null) {
            try {
                File file = new File(imgPath, imgName);//可以是任何图片格式.jpg,.png等
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = instreams.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                stateInt = 0;
                e.printStackTrace();
            } finally {
            }
        }
        return stateInt;
    }


}
