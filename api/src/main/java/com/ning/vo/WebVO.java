package com.ning.vo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Setter
@Getter
public class WebVO {
    Long id;
    private String name;
}
