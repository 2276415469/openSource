package com.ning.Do;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_user")
@Data
public class SysUserDO {
    Long id;
    String name;
}
