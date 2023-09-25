package com.hacker.service;


import com.hacker.domain.*;
import com.hacker.domain.enums.*;
import com.hacker.dto.*;
import com.hacker.dto.request.*;
import com.hacker.exception.*;
import com.hacker.exception.message.*;
import com.hacker.mapper.UserMapper;
import com.hacker.repository.*;
import com.hacker.security.*;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final ServiceOfferService serviceOfferService;


    public UserService(UserRepository userRepository, RoleService roleService,
                       @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper, ServiceOfferService serviceOfferService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.serviceOfferService = serviceOfferService;
    }

    public User getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException(
                        String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION, email)));
        return user;
    }

    public void saveUser(RegisterRequest registerRequest){
        //!!! DTO dan gelen username sistemde daha önce var mı ???
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new ConflictException(
                    String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,
                            registerRequest.getEmail())
            );
        }

        // !!! yeni kullanıcın rol bilgisini default olarak customer atıyorum
        Role role = roleService.findByType(RoleType.ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        //!!! Db ye gitmeden önce şifre encode edilecek
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        //!!! yeni kullanıcının gerekli bilgilerini setleyip DB ye gönderiyoruz
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPassword(encodedPassword);
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setRoles(roles);

        userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = userMapper.map(users);
        return userDTOs;
    }

    public UserDTO getPrincipal() {
        User user = getCurrentUser();
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return userDTO;

    }

    public User getCurrentUser(){
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
        User user = getUserByEmail(email);
        return user;
    }

    //Admin Fähigkeiten!
    public Page<UserDTO> getUserPage(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return getUserDTOPage(userPage);
    }

    private Page<UserDTO> getUserDTOPage(Page<User> userPage){
        return userPage.map(
                user -> userMapper.userToUserDTO(user));
    }


    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));
        return userMapper.userToUserDTO(user);
    }

    //Password erneuern
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest, Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_EXCEPTION));

        // 1- Form a girilen OldPassword dogru mu?
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) { //alt, neu -> Vergleiche
            throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED_MESSAGE);
        }

        // 2- Yeni gelen sifreyi encode et
        String hashedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
            user.setPassword(hashedPassword);
            userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserUpdateRequest userUpdateRequest, Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_EXCEPTION));


        // 2- Email control!
        boolean emailExist = userRepository.existsByEmail(userUpdateRequest.getEmail()); // old Email: A = New Email: A  , richtig!
        if (emailExist && !userUpdateRequest.getEmail().equals(user.getEmail())){        // old Email: A = New Email: D (existiert), false!
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, userUpdateRequest.getEmail()));
        }

        userRepository.update(user.getId(),
                userUpdateRequest.getFirstName(),
                userUpdateRequest.getLastName(),
                userUpdateRequest.getUsername(),
                userUpdateRequest.getEmail());
    }

    public void updateUserAuth(Integer id, @Valid AdminUserUpdateRequest adminUserUpdateRequest) {
        User user = getById(id);

        // 2- Username control!
        boolean emailExist = userRepository.existsByEmail(adminUserUpdateRequest.getEmail()); // old Email: A = New Email: A  , richtig!
        if (emailExist && !adminUserUpdateRequest.getEmail().equals(user.getEmail())){        // old Email: A = New Email: D (existiert), false!
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,adminUserUpdateRequest.getEmail()));
        }

        // 3- Password Control!!
        if (adminUserUpdateRequest.getPassword()==null){  //DB den password'i alip null olmasindan kurtariyoruz
            adminUserUpdateRequest.setPassword(user.getPassword());
        } else {
            String encodedPassword =
                    passwordEncoder.encode(adminUserUpdateRequest.getPassword()); //wenn admin ein neues Password hinzugefügt hat, dann encode (verschlüsseln)
            adminUserUpdateRequest.setPassword(encodedPassword);                // diese dann neu set im DTO
        }
        // Role Bilgisi
        Set<String> userStrRoles = adminUserUpdateRequest.getRoles();

        Set<Role> roles = convertRoles(userStrRoles);

        user.setFirstName(adminUserUpdateRequest.getFirstName());
        user.setLastName(adminUserUpdateRequest.getLastName());
        user.setPassword(adminUserUpdateRequest.getPassword());
        user.setUsername(adminUserUpdateRequest.getUsername());
        user.setEmail(adminUserUpdateRequest.getEmail());
        user.setRoles(roles);

        userRepository.save(user);
    }

    private Set<Role> convertRoles(Set<String> pRoles){
        Set<Role> roles = new HashSet<>();

        if(pRoles==null){
            Role userRole = roleService.findByType(RoleType.USER);
            roles.add(userRole);
        } else {
            pRoles.forEach(roleStr->{
                if(roleStr.equals(RoleType.ADMIN.getName())){
                    Role adminRole = roleService.findByType(RoleType.ADMIN);
                    roles.add(adminRole);
                } else {
                    Role userRole = roleService.findByType(RoleType.USER);
                    roles.add(userRole);
                }
            });
        }
        return roles;
    }

    //yardimci method -> privat, sadece burada kullanacaz!!!
    public User getById(Integer id){
        User user = userRepository.findUserById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));
    return user;
    }

    public void removeUserById(Integer id) {
        userRepository.deleteById(id);

    }

}