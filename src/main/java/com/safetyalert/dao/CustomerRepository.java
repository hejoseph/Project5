package com.safetyalert.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.safetyalert.model.Customer;
import com.safetyalert.model.id.PersonId;

public interface CustomerRepository extends CrudRepository<Customer, PersonId> {

  List<Customer> findByLastName(String lastName);
  Customer findByFirstNameAndLastName(String firstName, String lastName);

  Customer findById(long id);
}
