package com.nikola;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Nikola on 23/05/2016.
 */
public interface ContactRepository extends CrudRepository<Contact, Long> {

    Page<Contact> findAll(Pageable pageable);

    Contact findByFirstName(String firstName);

    List<Contact> findByAccountId(Long id);
}
