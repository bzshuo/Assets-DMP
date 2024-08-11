package com.mengs.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用章管理表
 * </p>
 *
 * @author zyz
 * @since 2023-05-07
 */
@Getter
@Setter
  @ApiModel(value = "Usingseal对象", description = "用章管理表")
public class Usingseal implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("用章id")
      @TableId(value = "seal_id", type = IdType.AUTO)
      private Integer sealId;

      @ApiModelProperty("用章名称")
      private String sealName;

      @ApiModelProperty("用章类型")
      private String type;

      @ApiModelProperty("用章图片地址")
      private String urlPath;

      @ApiModelProperty("状态:颁发 失效")
      private String state;

      @ApiModelProperty("用章人")
      private String staff;

      @ApiModelProperty("创建时间")
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
      private Date createTime;


}
