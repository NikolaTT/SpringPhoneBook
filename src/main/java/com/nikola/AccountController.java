package com.nikola;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikola on 23/05/2016.
 */

@RestController
public class AccountController {


    private final AccountRepository accountRepository;
    private final ContactRepository contactRepository;
    private final SearchRepository searchRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    AccountController(AccountRepository accountRepository, ContactRepository contactRepository, SearchRepository searchRepository) {
        this.accountRepository = accountRepository;
        this.contactRepository = contactRepository;
        this.searchRepository = searchRepository;
    }


    @RequestMapping("/hello")
    public String hello() {
        return "hello" + request.getSession().getAttribute("user").toString();
    }

    @RequestMapping("/logout")
    public String logout2() {
        return "logout";
    }

    @RequestMapping("/login2/{username}/{password}")
    public String login2(@PathVariable String username, @PathVariable String password) {
        String response = "";

        Account acc = accountRepository.findByUsername(username);

        if (null != acc) {
            request.getSession().setAttribute("user", acc.getUsername());
            return hello();
        } else response = "User not found";


        return response;
    }

    @RequestMapping("/addContactSpring")
    public void addContact(@RequestBody Contact contact) {
        contact.setAccountId(accountRepository.findByUsername(request.getSession().getAttribute("user").toString()).getId());
        contactRepository.save(contact);
    }

    @RequestMapping("/showContactsSpring")
    public Map<String, Iterable<Contact>> showContacts() {

        Map<String, Iterable<Contact>> data = new HashMap<>();

        Account account = accountRepository.findByUsername(request.getSession().getAttribute("user").toString());

        data.put("data", contactRepository.findByAccountId(account.getId()));


        return data;
    }

    @RequestMapping("/getSearches")
    public Map<String, Iterable<Search>> getSearches() {

        Map<String, Iterable<Search>> data = new HashMap<>();

        Account account = accountRepository.findByUsername(request.getSession().getAttribute("user").toString());

        data.put("data", searchRepository.findByAccountId(account.getId()));

        return data;
    }

    @RequestMapping("/addSearch")
    public void addSearch(@RequestBody Search search) {
        search.setAccountId(accountRepository.findByUsername(request.getSession().getAttribute("user").toString()).getId());
        searchRepository.save(search);
    }

    @RequestMapping("/add/{username}/{password}")
    public String add(@PathVariable String username, @PathVariable String password) {

        String response = "";

        accountRepository.save(new Account(username, password));

        response += accountRepository.findByUsername(username).toString();

//        for (Account customer : accountRepository.findByUsername(username)) {
//            response += customer.toString() + "\n";
//        }

        return response;
    }
}
