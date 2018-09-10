package com.ifast.controller;


import java.util.Arrays;

import com.ifast.service.ProductService;
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
import com.ifast.domain.AboutDO;
import com.ifast.service.AboutService;
import com.ifast.common.utils.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * <pre>
 * 
 * </pre>
 * <small> 2018-09-04 14:59:58 | Aron</small>
 */
@Controller
@RequestMapping("/ifast/about")
public class AboutController extends AdminBaseController {
	@Autowired
	private AboutService aboutService;
     @Autowired
	private ProductService productService;
	
	@GetMapping()
	@RequiresPermissions("ifast:about:about")
	String About(){
	    return "ifast/about/about";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("ifast:about:about")
	public Result<Page<AboutDO>> list(AboutDO aboutDTO){
        Wrapper<AboutDO> wrapper = new EntityWrapper<AboutDO>(aboutDTO);
        Page<AboutDO> page = aboutService.selectPage(getPage(AboutDO.class), wrapper);
        return Result.ok(page);
	}
	
	@GetMapping("/add")
	@RequiresPermissions("ifast:about:add")
	String add(){
	    return "ifast/about/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("ifast:about:edit")
	String edit(@PathVariable("id") Long id,Model model){
		AboutDO about = aboutService.selectById(id);
		model.addAttribute("about", about);
	    return "ifast/about/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("ifast:about:add")
	public Result<String> save( AboutDO about){
		aboutService.insert(about);
        return Result.ok();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("ifast:about:edit")
	public Result<String>  update( AboutDO about){
		aboutService.updateById(about);
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("ifast:about:remove")
	public Result<String>  remove( Long id){
		aboutService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("ifast:about:batchRemove")
	public Result<String>  remove(@RequestParam("ids[]") Long[] ids){
		aboutService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}

	@ResponseBody
	@PostMapping("/upload")
	public Result<String> upload(@RequestParam("file") MultipartFile file,String content,String id) {
		String name=productService.upload(file);
		AboutDO aboutDO=new AboutDO();
		aboutDO.setContent(content);
		aboutDO.setPath(name);
		aboutDO.setId(Long.parseLong(id));
		this.aboutService.insertOrUpdate(aboutDO);
		return Result.ok();
	}
	
}
