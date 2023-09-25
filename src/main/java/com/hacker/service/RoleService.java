package com.hacker.service;

import com.hacker.domain.Role;
import com.hacker.domain.enums.RoleType;
import com.hacker.exception.ResourceNotFoundException;
import com.hacker.exception.message.ErrorMessage;
import com.hacker.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByType(RoleType roleType){
        Role role = roleRepository.findByType(roleType).orElseThrow(()->
                new ResourceNotFoundException(
                        String.format(ErrorMessage.ROLE_NOT_FOUND_EXCEPTION, roleType.name())));
        return role;
    }

}
