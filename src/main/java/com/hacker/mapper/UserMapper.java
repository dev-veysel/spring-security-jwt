package com.hacker.mapper;


import com.hacker.domain.*;
import com.hacker.dto.*;
import org.mapstruct.*;

import java.util.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // User -> UserDTO
    UserDTO userToUserDTO(User user);

    //Mehr Aufrufe
    List<UserDTO> map(List<User> userList);
}
