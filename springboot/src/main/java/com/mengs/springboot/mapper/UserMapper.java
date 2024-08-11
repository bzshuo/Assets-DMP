package com.mengs.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengs.springboot.controller.dto.UserPasswordDTO;
import com.mengs.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 用户基本表 Mapper 接口
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
public interface UserMapper extends BaseMapper<User> {

    @Update("update user set password = #{newPassword},update_time = sysdate() where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);

    @Select("select * from user")
    List<User> selectAll();

    @Select("select * from user where YEAR(create_time) = #{dateValue}")
    List<User> selectCountByDate(String dateValue);

    Page<User> findPage(Page<User> page, @Param("nickname") String nickname, @Param("email") String email, @Param("phone") String phone);

    @Select("select user_id from user where username = #{username}")
    Integer selectByName(String username);

    @Select("select distinct YEAR(create_time) from user group by create_time")
    List<String> getDateInfo();
}
