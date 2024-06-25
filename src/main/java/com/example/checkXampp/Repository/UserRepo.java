package com.example.checkXampp.Repository;

import com.example.checkXampp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {

}
