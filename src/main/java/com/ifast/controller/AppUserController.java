package com.ifast.controller;


import java.util.Arrays;

import com.ifast.domain.ApiUserDO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.common.base.AdminBaseController;
import com.ifast.service.UserService;
import com.ifast.common.utils.Result;

import javax.annotation.Resource;

/**
 * 
 * <pre>
 * @author
 * </pre>
 * <small> 2018-08-19 19:10:03 | Aron</small>
 */
@Controller
@RequestMapping("/ifast/user")
public class AppUserController extends AdminBaseController {
	@Resource(name="apiService")
	private UserService userService;
	
	@GetMapping()
	@RequiresPermissions("ifast:user:user")
	String User(){
	    return "ifast/user/user";
	}

	@GetMapping("/black")
	@RequiresPermissions("ifast:user:user")
	public String black(){
		return "ifast/user/black";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("ifast:user:user")
	public Result<Page<ApiUserDO>> list(ApiUserDO userDTO){
		userDTO.setType("0");
        Wrapper<ApiUserDO> wrapper = new EntityWrapper<>(userDTO);
        Page<ApiUserDO> page = userService.selectPage(getPage(ApiUserDO.class), wrapper.orderBy("CreateTime"));
        return Result.ok(page);
	}
	@ResponseBody
	@GetMapping("/blacklist")
	@RequiresPermissions("ifast:user:user")
	public Result<Page<ApiUserDO>> blacklist(ApiUserDO userDTO){
		userDTO.setType("1");
		Wrapper<ApiUserDO> wrapper = new EntityWrapper<>(userDTO);
		Page<ApiUserDO> page = userService.selectPage(getPage(ApiUserDO.class), wrapper.orderBy("CreateTime"));
		return Result.ok(page);
	}
	
	@GetMapping("/add")
	@RequiresPermissions("ifast:user:add")
	String add(){
	    return "ifast/user/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("ifast:user:edit")
	String edit(@PathVariable("id") Long id,Model model){
		ApiUserDO user = userService.selectById(id);
		model.addAttribute("user", user);
	    return "ifast/user/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("ifast:user:add")
	public Result<String> save( ApiUserDO user){
		userService.insert(user);
        return Result.ok();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("ifast:user:edit")
	public Result<String>  update( ApiUserDO user){
		userService.updateById(user);
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("ifast:user:remove")
	public Result<String>  remove( Long id){
		userService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("ifast:user:batchRemove")
	public Result<String>  remove(@RequestParam("ids[]") Long[] ids){
		userService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}

	/**
	 * 加入黑名单
	 */
	@PostMapping( "/addBlackList")
	@ResponseBody
	public Result<String>  addBlackList( Long id){
		ApiUserDO userDO=new ApiUserDO();
		userDO.setType("1");
		userDO.setId(id);
		userService.updateById(userDO);
		return Result.ok();
	}
	/**
	 * 加入黑名单
	 */
	@PostMapping( "/removeBlackList")
	@ResponseBody
	public Result<String>  removeBlackList( Long id){
		ApiUserDO userDO=new ApiUserDO();
		userDO.setType("0");
		userDO.setId(id);
		userService.updateById(userDO);
		return Result.ok();
	}
	
}
