package com.ifast.api.controller;

import com.ifast.common.utils.Result;
import com.ifast.domain.ApiUserDO;
import com.ifast.domain.AttentionDO;
import com.ifast.domain.ProductDO;
import com.ifast.domain.UnitDO;
import com.ifast.service.AttentionService;
import com.ifast.service.ProductService;
import com.ifast.service.UnitService;
import com.ifast.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("login")
    @ApiOperation("api登录")
    public Result<?> login(String code){
        String str="";
        try {
            str= userService.getToken(code);
        } catch (IOException e) {
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
    public ApiUserDO queryUser(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token) {
        return userService.getUserByToken(token);
    }

    @GetMapping("/queryUnit")
    @ApiOperation("查询公司信息")
    public UnitDO queryUnit(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token) {
        return unitService.getUnitByToken(token);
    }

    @GetMapping("/queryProduct")
    @ApiOperation("查询产品")
    public ProductDO queryProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token) {
        return productService.getProductByToken(token);
    }

    @PostMapping("saveUser")
    @ApiOperation("保存个人信息")
    public Result<?> saveUser(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, ApiUserDO apiUserDO) {
        userService.insert(apiUserDO);
        return Result.ok();
    }

    @PostMapping("/saveUnit")
    @ApiOperation("保存公司信息")
    public Result<?> saveUnit(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, UnitDO unitDO) {
        ApiUserDO userDO = userService.getUserByToken(token);
        unitDO.setUserId(userDO.getId());
        unitService.insert(unitDO);
        return Result.ok();
    }

    @PostMapping("/saveProduct")
    @ApiOperation("保存产品信息")
    public Result<?> saveProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, ProductDO productDO) {
        productService.insert(productDO);
        return Result.ok();
    }

    @PostMapping("/updateUser")
    @ApiOperation("更新用户信息")
    public Result<?> updateUser(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, ApiUserDO apiUserDO) {
        userService.updateById(apiUserDO);
        return Result.ok();
    }

    @PostMapping("/updateUnit")
    @ApiOperation("更新公司信息")
    public Result<?> updateUnit(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, UnitDO unitDO) {
        unitService.updateById(unitDO);
        return Result.ok();
    }

    @PostMapping("/updateProduct")
    @ApiOperation("更新产品信息")
    public Result<?> updateProduct(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, ProductDO productDO) {
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
    @ApiOperation("关注产品信息")
    public Result<?> attention(@ApiParam(name = "Authorization", required = true, value = "token") @RequestHeader("Authorization") String token, String id){
       ApiUserDO userDO= userService.getUserByToken(token);
        AttentionDO attentionDO=new AttentionDO();
        attentionDO.setMid(userDO.getId());
        attentionDO.setTid(Long.getLong(id));
        attentionService.insert(attentionDO);
        return Result.ok();
    }
}
