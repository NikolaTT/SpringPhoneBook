package com.nikola;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Nikola on 23/05/2016.
 */
public interface SearchRepository extends CrudRepository<Search, Long> {

    Page<Search> findAll(Pageable pageable);

    Search findByType(String username);

    List<Search> findByAccountId(Long id);
}
