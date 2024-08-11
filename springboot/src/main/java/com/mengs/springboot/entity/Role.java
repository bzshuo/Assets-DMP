package com.mengs.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
@Getter
@Setter
  @ApiModel(value = "Role对象", description = "权限表")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("权限id")
        @TableId(value = "role_id", type = IdType.AUTO)
      private Integer roleId;

      @ApiModelProperty("权限名称")
      private String name;

      @ApiModelProperty("权限等级")
      private String grade;

      @ApiModelProperty("权限说明")
      private String comment;


}
