package com.ifast.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.api.dao.ApiActiveDao;
import com.ifast.api.pojo.domain.ActiveDO;
import com.ifast.common.base.AdminBaseController;
import com.ifast.common.domain.DictDO;
import com.ifast.common.service.DictService;
import com.ifast.common.utils.Result;
import com.ifast.domain.ApiUserDO;
import com.ifast.service.ActiveService;
import com.ifast.service.UnitService;
import com.ifast.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
	@Autowired
	private DictService dictService;
	@Resource(name="apiService")
	private UserService userService;

	@GetMapping()
	@RequiresPermissions("ifast:active:active")
	ModelAndView Active(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ifast/active/active");
		List<String> sheng = this.activeService.sheng();
		List<String> shi = this.activeService.shi();
		List<String> qu = this.activeService.qu();
		modelAndView.addObject("sheng", sheng);
		modelAndView.addObject("shi", shi);
		modelAndView.addObject("qu", qu);
		Wrapper<DictDO> wrapper = new EntityWrapper<>();
		wrapper.eq("type", "active");
		List<DictDO> types = this.dictService.selectList(wrapper);
		modelAndView.addObject("type", types);
		Wrapper<ActiveDO> wrapper1 = new EntityWrapper<>();
		int count = this.activeService.selectCount(wrapper1);
		modelAndView.addObject("count", count);
		return modelAndView;
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("ifast:active:active")
	public Result<Page<ActiveDO>> list(ActiveDO activeDTO){
        Wrapper<ActiveDO> wrapper = new EntityWrapper<ActiveDO>(activeDTO);
      /*  if(StringUtils.isNotBlank(activeDTO.getTitle())){
			wrapper.like("title", activeDTO.getTitle());
//			activeDTO.setTitle(null);
		}else {
        	activeDTO.setTitle(null);
		}
		if (StringUtils.isBlank(activeDTO.getProvince())){
        	activeDTO.setProvince(null);
		}
		if (StringUtils.isBlank(activeDTO.getCity())){
        	activeDTO.setCity(null);
		}
		if (StringUtils.isBlank(activeDTO.getCounty())){
        	activeDTO.setCounty(null);
		}
		if (StringUtils.isBlank(activeDTO.getType())){
			activeDTO.setType(null);
		}*/
        //Page<ActiveDO> page = activeService.selectPage(getPage(ActiveDO.class), wrapper);
		Page<ActiveDO> page =activeService.queryByPage(getPage(ActiveDO.class),activeDTO);
		return Result.ok(page);
	}
	
	@GetMapping("/add")
	@RequiresPermissions("ifast:active:add")
	String add(Model model){
		Wrapper<DictDO> wrapper = new EntityWrapper<>();
		wrapper.eq("type", "active");
		List<DictDO> disc = this.dictService.selectList(wrapper);
		model.addAttribute("dics", disc);
	    return "ifast/active/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("ifast:active:edit")
	String edit(@PathVariable("id") String id,Model model){
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
	    active.setCreateUserId(this.getUserId());
		activeService.insert(active);
        return Result.ok();
	}
	/**
	 * 开启关闭
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("ifast:active:edit")
	public Result<String>  update( Long id){
		ActiveDO activeDO =activeService.selectById(id);
		if (Objects.equals(activeDO.getStop(), "0")) {
			activeDO.setStop("1");
		}else {
			activeDO.setStop("0");
		}
		activeService.updateById(activeDO );
		return Result.ok();
	}

	/**
	 * 开启关闭
	 */
	@ResponseBody
	@RequestMapping("/updateData")
	@RequiresPermissions("ifast:active:edit")
	public Result<String>  updateData( ActiveDO activeDO){
		activeService.updateById(activeDO );
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("ifast:active:remove")
	public Result<String>  remove( String id){
		Wrapper<ActiveDO> activeDOWrapper = new EntityWrapper<>();
		activeDOWrapper.eq("id", id);
		activeService.delete(activeDOWrapper);

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

	@RequestMapping("/renyuan")
	@ResponseBody
	public ModelAndView   user(String activeId){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ifast/active/user");
		modelAndView.addObject("activeId", activeId);return modelAndView;
	}

	@ResponseBody
	@GetMapping("/user/list")
	@RequiresPermissions("ifast:user:user")
	public Result<Page<ApiUserDO>> list(ApiUserDO userDTO){
		userDTO.setType("0");
		Page<ApiUserDO> page = userService.queryUserPage(getPage(ApiUserDO.class),userDTO);
		return Result.ok(page);
	}
	
}
