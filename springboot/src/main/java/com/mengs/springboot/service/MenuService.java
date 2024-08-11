package com.mengs.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengs.springboot.entity.Menu;
import com.mengs.springboot.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {

    @Autowired
    MenuMapper menuMapper;

    public List<Menu> selectMenus(String menuId){
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("weight");
        // 查询所有数据
        List<Menu> list = menuMapper.selectList(queryWrapper);
        return list;
    }
}
