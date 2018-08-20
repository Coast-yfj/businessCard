package com.ifast.api.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifast.api.pojo.dto.UserLoginDTO;
import com.ifast.api.pojo.vo.TokenVO;
import com.ifast.api.service.UserService;
import com.ifast.common.utils.Result;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <pre>
 *  基于jwt实现的API测试类
 * </pre>
 * 
 * <small> 2018年4月27日 | Aron</small>
 */
@RestController
@RequestMapping("/api/user/")
//@Api(tags = { "测试API" })
public class ApiUserController{
    @Autowired
    private UserService userService;

//    @PostMapping("login")
//////    @Log("api测试-登录")
////    @ApiOperation("api测试-登录")
////    public Result<?> token(@RequestBody final UserLoginDTO loginDTO) {
////        TokenVO token = userService.getToken(loginDTO.getUname(), loginDTO.getPasswd());
////        return Result.ok(token);
////    }
    @PostMapping("login")
    @ApiOperation("api登录")
    public Result<?> login(String code){
        return Result.ok();
    }
    
    @PostMapping("refresh")
//    @Log("api测试-刷新token")
    @ApiOperation("api测试-刷新token")
    public Result<?> refresh(@RequestParam String uname, @RequestBody final String refresh_token) {
    	TokenVO token = userService.refreshToken(uname, refresh_token);
    	return Result.ok(token);
    }
    
    @PostMapping("logout")
//    @Log("api测试-刷新token")
    @ApiOperation("api测试-注销token")
    public Result<?> logout(String token, String refresh_token) {
    	Boolean expire = userService.logoutToken(token, refresh_token);
    	return Result.ok(expire);
    }

    @GetMapping("/require_auth")
    @RequiresAuthentication
//    @Log("api测试-需要认证才能访问")
    @ApiOperation("api测试-需要认证才能访问")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "Authorization", paramType = "header") })
    public Result<?> requireAuth() {
        return Result.build(200, "认证通过", null);
    }

    @GetMapping("/require_role")
    @RequiresRoles("apiRole")
//    @Log("api测试-需要api角色才能访问")
    @ApiOperation("api测试-需要api角色才能访问")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "Authorization", paramType = "header") })
    public Result<?> requireRole() {
        return Result.build(200, "用户有role角色权限", null);
    }

    @GetMapping("/require_permission")
    @RequiresPermissions("api:user:update")
//    @Log("api测试-需要api:user:update权限才能访问")
    @ApiOperation("api测试-需要api:user:update权限才能访问")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", value = "Authorization", paramType = "header") })
    public Result<?> requirePermission() {
        return Result.build(200, "用户有api:user:update权限", null);
    }

}
