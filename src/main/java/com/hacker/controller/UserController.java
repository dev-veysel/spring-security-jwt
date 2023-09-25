package com.hacker.controller;


import com.hacker.dto.UserDTO;
import com.hacker.dto.message.CompanyResponse;
import com.hacker.dto.message.ResponseMessage;
import com.hacker.dto.request.AdminUserUpdateRequest;
import com.hacker.dto.request.UpdatePasswordRequest;
import com.hacker.dto.request.UserUpdateRequest;
import com.hacker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/v3/user")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    // get all Users
    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
         List<UserDTO> allUsers = userService.getAllUsers();

    return ResponseEntity.ok(allUsers);
    }

    // welcher user ist gerade eingeloggt
    @GetMapping("/auth/actualuser")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UserDTO> getUser() {
        UserDTO userDTO = userService.getPrincipal();

    return ResponseEntity.ok(userDTO);
    }

    // get all Users With Page
    @GetMapping("/auth/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsersByPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value="direction",
                    required = false,
                    defaultValue = "DESC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<UserDTO> userDTOPage = userService.getUserPage(pageable);

        return ResponseEntity.ok(userDTOPage);
    }

    // get User by id
    @GetMapping("/auth/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById (@PathVariable Integer id){
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    // update password!
    @PatchMapping("/auth/update_password/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<CompanyResponse> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest, @PathVariable Integer id){
        userService.updatePassword(updatePasswordRequest, id);

        CompanyResponse response = new CompanyResponse();
        response.setMessage(ResponseMessage.PASSWORD_CHANGED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }

    //Update User
    @PutMapping("/auth/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<CompanyResponse> updateUser(
            @Valid @RequestBody UserUpdateRequest userUpdateRequest, @PathVariable Integer id){
        userService.updateUser(userUpdateRequest, id);

        CompanyResponse response = new CompanyResponse();
        response.setMessage(ResponseMessage.USER_UPDATE_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }

    // Admin updated irgendein User
    @PutMapping("/auth/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyResponse> updateUserAuth (  @PathVariable Integer id,
                                @Valid @RequestBody AdminUserUpdateRequest adminUserUpdateRequest){

        userService.updateUserAuth(id, adminUserUpdateRequest);

        CompanyResponse response = new CompanyResponse();
        response.setMessage(ResponseMessage.USER_UPDATE_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }

    // delete user
    @DeleteMapping("/auth/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyResponse> deleteUser(@PathVariable Integer id){
        userService.removeUserById(id);

        CompanyResponse response = new CompanyResponse();
        response.setMessage(ResponseMessage.USER_DELETE_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }
}