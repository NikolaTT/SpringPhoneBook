package com.nikola;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Nikola on 23/05/2016.
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    Page<Account> findAll(Pageable pageable);

    Account findByUsername(String username);
    
    Account findByUsernameAndPassword(String username, String password);
}
