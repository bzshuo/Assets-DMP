package com.mengs.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengs.springboot.common.Result;
import com.mengs.springboot.service.MenuService;
import org.assertj.core.internal.Dates;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import com.mengs.springboot.entity.Menu;

import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Menu menu) {
        menuService.saveOrUpdate(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            menuService.removeById(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error("该菜单已被赋予角色使用，不可删除！");
        }
    }

    @GetMapping("/ids")
    public Result findAllIds() {
        return Result.success(menuService.list().stream().map(Menu::getMenuId));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        try {
            menuService.removeByIds(ids);
            return Result.success();
        } catch (Exception e) {
            return Result.error("该菜单已被赋予角色使用，不可删除！");
        }
    }

    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        return Result.success(menuService.selectMenus(name));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByAsc("weight");
        return Result.success(menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    public static void main(String[] args) {
        System.out.println(makeRandom());
        System.out.println(makeTransId());
    }

    private static String makeTransId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timeStamp = sdf.format(new Date());
        return timeStamp + "-" + makeRandom();
    }

    private static String makeRandom() {
        int random = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(random);
    }
//
//    private static String makeTimeStamp(){
//        return Dates.format(new Date(),"yyyy-MM-dd HH:mm:ss");
//    }

}

