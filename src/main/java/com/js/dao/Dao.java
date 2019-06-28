package com.js.dao;

import com.js.entity.UserDetails;
import org.springframework.data.repository.CrudRepository;

public interface Dao extends CrudRepository<UserDetails, String> {
    UserDetails findByEMail(String email);
}
