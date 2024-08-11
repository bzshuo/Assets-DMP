package com.mengs.springboot.mapper;

import com.mengs.springboot.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单 Mapper 接口
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
public interface MenuMapper extends BaseMapper<Menu> {

    @Select("select * from menu where menu_id ki")
    List<Menu> selectMenuList(List<Integer> menuIds);
}
