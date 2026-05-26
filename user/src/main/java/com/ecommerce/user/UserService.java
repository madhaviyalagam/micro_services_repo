package com.ecommerce.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    public  final UserRepository userRepositpry;
   static List<User> userList = new ArrayList<>();

   public List<UserResponse> getUserList(){
       List<User> all = userRepositpry.findAll();
       return all.stream().map(this::mapToUserResponse).toList();
    }
   public List<User> createUser(User user){
       userRepositpry.save(user);
        return userRepositpry.findAll();
    }

    public Optional<User> getUser(Long id) {
      return userRepositpry.findById(id);
    }
    public boolean updateUser(Long id, User user){
       return userRepositpry.findById(id)
               .map(existingUser ->{
           existingUser.setFristName(user.getFristName());
           existingUser.setLastName(user.getLastName());
           userRepositpry.save(user);
           return true;
       }).orElse(false);
    }
    private UserResponse mapToUserResponse(User user){
       UserResponse response = new UserResponse();
       response.setId(user.getId());
       response.setFristName(user.getFristName());
       response.setLastName(user.getLastName());
       response.setEmail(user.getEmail());
       response.setPhone_number(user.getPhone_number());
        Optional.ofNullable(user.getAddresses())
                .ifPresent(addresses -> {
                    AddressDto addressDto = new AddressDto();
                    addressDto.setStreet(user.getAddresses().getStreet());
                    addressDto.setCity(user.getAddresses().getCity());
                    addressDto.setState(user.getAddresses().getState());
                    addressDto.setCountry(user.getAddresses().getCountry());
                    addressDto.setZipcode(user.getAddresses().getZipcode());
                    response.setAddressDto(addressDto);
                });
       return response;
    }
}
