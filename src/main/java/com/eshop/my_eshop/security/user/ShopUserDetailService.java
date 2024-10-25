package com.eshop.my_eshop.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eshop.my_eshop.model.User;
import com.eshop.my_eshop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopUserDetailService implements UserDetailsService{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(()-> new UsernameNotFoundException("Username not found"));

        return ShopUserDetails.buildUserDetails(user);

    }
    
}
