package com.zjy.mapstruct.mapping.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zjy
 * @date 2022/5/19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String name;
    private Integer age;
    private Integer time;
}