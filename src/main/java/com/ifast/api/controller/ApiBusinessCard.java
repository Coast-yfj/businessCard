package com.ifast.api.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ifast.api.util.JWTUtil;
import com.ifast.common.utils.Result;
import com.ifast.domain.*;
import com.ifast.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author yfj
 */
@RestController
@RequestMapping("/api/businessCard/")
public class ApiBusinessCard {

    @Resource(name = "apiService")
    private UserService userService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AttentionService attentionService;

    @Autowired
    private AboutService aboutService;

    @PostMapping("login")
    @ApiOperation("api登录")
    public Result<?> login(String code,String iv,String encryptedData){
        String str="";
        try {
            str= userService.getToken(code,iv,encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail() ;
        }
        if(str==""){
            return Result.fail();
        }
        return Result.ok(str);
    }

    @GetMapping("/queryUser")
    @ApiOperation("查询用户信息")
    public Result<?> queryUser(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token) {
        return Result.ok(userService.getUserByToken(token));
    }

    @GetMapping("/queryUnit")
    @ApiOperation("查询公司信息")
    public Result<?> queryUnit(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token) {
        return Result.ok(unitService.getUnitByToken(token));
    }

    @GetMapping("/queryProduct")
    @ApiOperation("查询产品")
    public Result<?> queryProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token) {
        return Result.ok(productService.getProductByToken(token));
    }

    @PostMapping("saveUser")
    @ApiOperation("保存个人信息")
    public Result<?> saveUser(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, ApiUserDO apiUserDO) {
       String userId= JWTUtil.getUserId(token);
       apiUserDO.setId(Long.parseLong(userId));
        userService.insertOrUpdate(apiUserDO);
        return Result.ok();
    }

    @PostMapping("/saveUnit")
    @ApiOperation("保存公司信息")
    public Result<?> saveUnit(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token,@RequestParam ("logo") MultipartFile file, UnitDO unitDO) {
        ApiUserDO userDO = userService.getUserByToken(token);
        unitDO.setUserId(userDO.getId());
        String url=productService.upload(file);
        unitDO.setLogo(url);
        unitService.insert(unitDO);
        return Result.ok(unitDO);
    }

    @PostMapping("/saveProduct")
    @ApiOperation("保存产品信息")
    public Result<?> saveProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, @RequestParam ("poster") MultipartFile file,ProductDO productDO) {
        String url=productService.upload(file);
        productDO.setPath(url);
        productService.insert(productDO);
        return Result.ok(productDO);
    }

    @PostMapping("/updateUser")
    @ApiOperation("更新用户信息")
    public Result<?> updateUser(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, ApiUserDO apiUserDO) {
        String userId= JWTUtil.getUserId(token);
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
    public Result<?> updateProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token,  ProductDO productDO) {
        productService.updateById(productDO);
        return Result.ok();
    }

    @PostMapping("/delProduct")
    @ApiOperation("删除产品信息")
    public Result<?> delProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, String id) {
        productService.deleteById(id);
        return Result.ok();
    }

    @PostMapping("/attention")
    @ApiOperation("关注名片信息")
    public Result<?> attention(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, String id){
       ApiUserDO userDO= userService.getUserByToken(token);
        AttentionDO attentionDO=new AttentionDO();
        attentionDO.setMid(userDO.getId());
        attentionDO.setTid(Long.getLong(id));
        attentionService.insert(attentionDO);
        return Result.ok();
    }
    @GetMapping("/queryCardHolder")
    @ApiOperation("查询名片夹信息")
    public Result<?> queryCardHolder(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token){
        String userId=JWTUtil.getUserId(token);
        return Result.ok(userService.queryByIds(userId));
    }
    @GetMapping("/queryAbout")
    @ApiOperation("关于我们信息")
    public Result<?> queryAbout(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token){

        return Result.ok(aboutService.selectOne(new EntityWrapper<>(new AboutDO())));
    }
}
