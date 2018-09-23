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
import com.ifast.domain.UnitDO;
import com.ifast.service.UnitService;
import com.ifast.common.utils.Result;

/**
 * 
 * <pre>
 * 
 * </pre>
 * <small> 2018-08-19 19:10:02 | Aron</small>
 */
@Controller
@RequestMapping("/ifast/unit")
public class UnitController extends AdminBaseController {
	@Autowired
	private UnitService unitService;
	
	@GetMapping()
	@RequiresPermissions("ifast:unit:unit")
	String Unit(){
	    return "ifast/unit/unit";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("ifast:unit:unit")
	public Result<Page<UnitDO>> list(UnitDO unitDTO){
        Wrapper<UnitDO> wrapper = new EntityWrapper<UnitDO>(unitDTO);
		unitDTO.setType("0");
        Page<UnitDO> page = unitService.selectPage(getPage(UnitDO.class), wrapper);
        return Result.ok(page);
	}
	@GetMapping("/black")
	@RequiresPermissions("ifast:unit:unit")
	public String black(){
		return "ifast/unit/black";
	}

	@ResponseBody
	@GetMapping("/blacklist")
	@RequiresPermissions("ifast:unit:unit")
	public Result<Page<UnitDO>> blacklist(UnitDO unitDTO){
		unitDTO.setType("1");
		Wrapper<UnitDO> wrapper = new EntityWrapper<UnitDO>(unitDTO);
		Page<UnitDO> page = unitService.selectPage(getPage(UnitDO.class), wrapper.orderBy("CreateTime"));
		return Result.ok(page);
	}
	
	@GetMapping("/add")
	@RequiresPermissions("ifast:unit:add")
	String add(){
	    return "ifast/unit/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("ifast:unit:edit")
	String edit(@PathVariable("id") Long id,Model model){
		UnitDO unit = unitService.selectById(id);
		model.addAttribute("unit", unit);
	    return "ifast/unit/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("ifast:unit:add")
	public Result<String> save( UnitDO unit){
		unitService.insert(unit);
        return Result.ok();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("ifast:unit:edit")
	public Result<String>  update( UnitDO unit){
		unitService.updateById(unit);
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("ifast:unit:remove")
	public Result<String>  remove( Long id){
		unitService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("ifast:unit:batchRemove")
	public Result<String>  remove(@RequestParam("ids[]") Long[] ids){
		unitService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
	/**
	 * 加入黑名单
	 */
	@PostMapping( "/addBlackList")
	@ResponseBody
	public Result<String>  addBlackList( Long id){
		UnitDO unitDO=new UnitDO();
		unitDO.setType("1");
		unitDO.setId(id);
		unitService.updateById(unitDO);
		return Result.ok();
	}
	/**
	 * 加入黑名单
	 */
	@PostMapping( "/removeBlackList")
	@ResponseBody
	public Result<String>  removeBlackList( Long id){
		UnitDO unitDO=new UnitDO();
		unitDO.setType("0");
		unitDO.setId(id);
		unitService.updateById(unitDO);
		return Result.ok();
	}
	
}
