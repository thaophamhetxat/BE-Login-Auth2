package com.example.beloginauth2.Repository;

import com.example.beloginauth2.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface  UserRepository extends CrudRepository<User, Long> {
    public User findByEmail(String email);

    public boolean existsByEmail(String email);


}
