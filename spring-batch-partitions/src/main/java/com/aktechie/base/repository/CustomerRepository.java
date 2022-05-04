package com.aktechie.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aktechie.base.entity.Customer;


public interface CustomerRepository extends CrudRepository<Customer,Integer>{

}
