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
import com.ifast.domain.AttentionDO;
import com.ifast.service.AttentionService;
import com.ifast.common.utils.Result;

/**
 * 
 * <pre>
 * 
 * </pre>
 * <small> 2018-08-19 22:42:09 | Aron</small>
 */
@Controller
@RequestMapping("/ifast/attention")
public class AttentionController extends AdminBaseController {
	@Autowired
	private AttentionService attentionService;
	
	@GetMapping()
	@RequiresPermissions("ifast:attention:attention")
	String Attention(){
	    return "ifast/attention/attention";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("ifast:attention:attention")
	public Result<Page<AttentionDO>> list(AttentionDO attentionDTO){
        Wrapper<AttentionDO> wrapper = new EntityWrapper<AttentionDO>(attentionDTO);
        Page<AttentionDO> page = attentionService.selectPage(getPage(AttentionDO.class), wrapper);
        return Result.ok(page);
	}
	
	@GetMapping("/add")
	@RequiresPermissions("ifast:attention:add")
	String add(){
	    return "ifast/attention/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("ifast:attention:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		AttentionDO attention = attentionService.selectById(id);
		model.addAttribute("attention", attention);
	    return "ifast/attention/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("ifast:attention:add")
	public Result<String> save( AttentionDO attention){
		attentionService.insert(attention);
        return Result.ok();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("ifast:attention:edit")
	public Result<String>  update( AttentionDO attention){
		attentionService.updateById(attention);
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("ifast:attention:remove")
	public Result<String>  remove( Integer id){
		attentionService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("ifast:attention:batchRemove")
	public Result<String>  remove(@RequestParam("ids[]") Integer[] ids){
		attentionService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
	
}
