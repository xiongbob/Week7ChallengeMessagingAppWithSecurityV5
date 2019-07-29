package com.example.demo;

import org.springframework.data.repository.CrudRepository;

//add on for week 7
public interface MessageRepository extends CrudRepository<Message, Long> {
}
