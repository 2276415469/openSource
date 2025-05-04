package com.ning.Do;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("test")
public class WebDO {
    Long id;
}
