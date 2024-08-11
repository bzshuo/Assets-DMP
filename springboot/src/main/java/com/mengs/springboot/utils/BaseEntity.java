package com.mengs.springboot.utils;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * @author ：ms(YX)
 * @description ：
 * @date ：Born in 2019/12/17 10:03
 */
public class BaseEntity {
    /**
     * 业务id
     */
    public String busId;

    public String getBusId() {
        return busId;
    }

    /** 便于查询日志 **/
    public void setBusId(String busId) {
        if (StringUtils.isNotEmpty(busId)) {
            this.busId = busId;
        } else {
            this.busId = String.valueOf(UUID.randomUUID()).replace("-", "");
        }
    }
}
