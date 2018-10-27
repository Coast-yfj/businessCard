package com.ifast.controller;


import java.util.Arrays;

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
import com.ifast.domain.HelpDO;
import com.ifast.service.HelpService;
import com.ifast.common.utils.Result;

/**
 * 
 * <pre>
 * 
 * </pre>
 * <small> 2018-10-27 13:39:31 | Aron</small>
 */
@Controller
@RequestMapping("/ifast/help")
public class HelpController extends AdminBaseController {
	@Autowired
	private HelpService helpService;
	
	@GetMapping()
	@RequiresPermissions("ifast:help:help")
	String Help(){
	    return "ifast/help/help";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("ifast:help:help")
	public Result<Page<HelpDO>> list(HelpDO helpDTO){
        Wrapper<HelpDO> wrapper = new EntityWrapper<HelpDO>(helpDTO);
        Page<HelpDO> page = helpService.selectPage(getPage(HelpDO.class), wrapper);
        return Result.ok(page);
	}
	
	@GetMapping("/add")
	@RequiresPermissions("ifast:help:add")
	String add(){
	    return "ifast/help/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("ifast:help:edit")
	String edit(@PathVariable("id") Long id,Model model){
		HelpDO help = helpService.selectById(id);
		model.addAttribute("help", help);
	    return "ifast/help/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("ifast:help:add")
	public Result<String> save( HelpDO help){
		helpService.insert(help);
        return Result.ok();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("ifast:help:edit")
	public Result<String>  update( HelpDO help){
		helpService.updateById(help);
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("ifast:help:remove")
	public Result<String>  remove( Long id){
		helpService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("ifast:help:batchRemove")
	public Result<String>  remove(@RequestParam("ids[]") Long[] ids){
		helpService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
	
}
