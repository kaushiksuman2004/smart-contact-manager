package com.smart.scm2.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.smart.scm2.Entities.Contact;

public interface ContactRepository extends CrudRepository<Contact, Integer> {

    // pagination
    // @Query("from Contact c where c.user.id = :userId")
    // public Page<Contact> findContactsByUser(@Param("userId") int userId);

    @Query("from Contact c where c.user.id = :userId")
    public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);

}
