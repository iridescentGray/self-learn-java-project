package com.zjy.mapstruct.mapping;

import com.zjy.mapstruct.mapping.domain.User;
import com.zjy.mapstruct.mapping.domain.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author zjy
 * @date 2022/5/19 13:17
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapExample {

    UserDTO toDto(User user);

    User toUser(UserDTO userDTO);
}
