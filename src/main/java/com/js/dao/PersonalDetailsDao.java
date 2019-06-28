package com.js.dao;

import com.js.entity.PersonalDetails;
import org.springframework.data.repository.CrudRepository;

public interface PersonalDetailsDao extends CrudRepository<PersonalDetails, String> {
}
