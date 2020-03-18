package com.safetyalert.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.safetyalert.model.Customer;
import com.safetyalert.model.Person;
import com.safetyalert.model.id.PersonId;

public interface PersonRepository extends CrudRepository<Person, PersonId> {

	List<Person> findByLastName(String lastName);
//  Customer findByFirstNameAndLastName(String firstName, String lastName);
//
//  Customer findById(long id);
}
