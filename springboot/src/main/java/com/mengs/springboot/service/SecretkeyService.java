package com.mengs.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengs.springboot.entity.Secretkey;
import com.mengs.springboot.mapper.SecretkeyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秘钥管理表 服务实现类
 * </p>
 *
 * @author zyz
 * @since 2023-05-07
 */
@Service
public class SecretkeyService extends ServiceImpl<SecretkeyMapper, Secretkey> {

}
