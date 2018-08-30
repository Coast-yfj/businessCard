package com.ifast.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.common.base.AdminBaseController;
import com.ifast.common.utils.Result;
import com.ifast.service.ActiveService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 
 * <pre>
 * 
 * </pre>
 * <small> 2018-08-29 22:44:49 | Aron</small>
 */
@Controller
@RequestMapping("/ifast/active")
public class ActiveController extends AdminBaseController {
	@Autowired
	private ActiveService activeService;
	
	@GetMapping()
	@RequiresPermissions("ifast:active:active")
	String Active(){
	    return "ifast/active/active";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("ifast:active:active")
	public Result<Page<ActiveDO>> list(ActiveDO activeDTO){
        Wrapper<ActiveDO> wrapper = new EntityWrapper<ActiveDO>(activeDTO);
        Page<ActiveDO> page = activeService.selectPage(getPage(ActiveDO.class), wrapper);
        return Result.ok(page);
	}
	
	@GetMapping("/add")
	@RequiresPermissions("ifast:active:add")
	String add(){
	    return "ifast/active/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("ifast:active:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		ActiveDO active = activeService.selectById(id);
		model.addAttribute("active", active);
	    return "ifast/active/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("ifast:active:add")
	public Result<String> save( ActiveDO active){
		activeService.insert(active);
        return Result.ok();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("ifast:active:edit")
	public Result<String>  update( ActiveDO active){
		activeService.updateById(active);
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("ifast:active:remove")
	public Result<String>  remove( Integer id){
		activeService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("ifast:active:batchRemove")
	public Result<String>  remove(@RequestParam("ids[]") Integer[] ids){
		activeService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
	
}
