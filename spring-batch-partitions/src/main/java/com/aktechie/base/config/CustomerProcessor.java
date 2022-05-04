package com.aktechie.base.config;

import org.springframework.batch.item.ItemProcessor;

import com.aktechie.base.entity.Customer;

public class CustomerProcessor implements ItemProcessor<Customer,Customer> {

	@Override
	public Customer process(Customer customer) throws Exception {
		return customer;
	}

}
