package com.mengs.springboot.mapper;

import com.mengs.springboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select role_id from role where grade = #{grade}")
    Integer selectByGrade(@Param("grade") String grade);
}
