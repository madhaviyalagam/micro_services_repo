package com.ecommerce.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api")
public class UserController {
   private final UserService userService;
   UserController(UserService userService){
       this.userService = userService;
   }

    @GetMapping("/get/users")
    ResponseEntity<List<UserResponse>> getUserList(){
       return  ResponseEntity.ok(userService.getUserList());
    }
    @GetMapping("/get/user/{id}")
    ResponseEntity<User> getUser(@PathVariable Long id){
       return userService.getUser(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    @PostMapping("/add/users")
    ResponseEntity<String> saveUserList(@RequestBody User user){
        userService.createUser(user);
        return new ResponseEntity<>("User succerssfully created", HttpStatus.CREATED);
    }
    @PutMapping("/update/user/{id}")
    ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user){
       if(userService.updateUser(id,user))
           return ResponseEntity.ok("User successfully Updated");
       else
           return ResponseEntity.notFound().build();
    }
}
