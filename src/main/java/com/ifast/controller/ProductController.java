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
import com.ifast.domain.ProductDO;
import com.ifast.service.ProductService;
import com.ifast.common.utils.Result;

/**
 * 
 * <pre>
 * 
 * </pre>
 * <small> 2018-08-19 19:10:02 | Aron</small>
 */
@Controller
@RequestMapping("/ifast/product")
public class ProductController extends AdminBaseController {
	@Autowired
	private ProductService productService;
	
	@GetMapping()
	@RequiresPermissions("ifast:product:product")
	String Product(){
	    return "ifast/product/product";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("ifast:product:product")
	public Result<Page<ProductDO>> list(ProductDO productDTO){
        Wrapper<ProductDO> wrapper = new EntityWrapper<ProductDO>(productDTO);
        Page<ProductDO> page = productService.selectPage(getPage(ProductDO.class), wrapper);
        return Result.ok(page);
	}
	
	@GetMapping("/add")
	@RequiresPermissions("ifast:product:add")
	String add(){
	    return "ifast/product/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("ifast:product:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		ProductDO product = productService.selectById(id);
		model.addAttribute("product", product);
	    return "ifast/product/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("ifast:product:add")
	public Result<String> save( ProductDO product){
		productService.insert(product);
        return Result.ok();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("ifast:product:edit")
	public Result<String>  update( ProductDO product){
		productService.updateById(product);
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("ifast:product:remove")
	public Result<String>  remove( Integer id){
		productService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("ifast:product:batchRemove")
	public Result<String>  remove(@RequestParam("ids[]") Integer[] ids){
		productService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
	
}
