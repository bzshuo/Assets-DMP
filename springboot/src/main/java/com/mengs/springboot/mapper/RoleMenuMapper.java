package com.mengs.springboot.mapper;

import com.mengs.springboot.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 权限菜单表 Mapper 接口
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Delete("delete from role_menu where role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    @Select("select menu_id from role_menu where role_id = #{roleId}")
    List<Integer> selectByRoleId(@Param("roleId")Integer roleId);

}
