package com.example.beloginauth2.Service;

import com.example.beloginauth2.Model.User;
import com.example.beloginauth2.Model.UserPrincaple;
import com.example.beloginauth2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        UserPrincaple userPrincaple = new UserPrincaple(user);
        return userPrincaple;
    }

    public boolean ifEmailExist(String mail) {
        return userRepository.existsByEmail(mail);
    }

    public User getUserByMail(String mail) {
        return userRepository.findByEmail(mail);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
