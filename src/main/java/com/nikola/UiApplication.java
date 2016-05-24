package com.nikola;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SpringBootApplication
@RestController
public class UiApplication {

    private AccountRepository accountRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }


    public static final Logger log = LoggerFactory.getLogger(UiApplication.class);

//    @RequestMapping("/user")
//    public Principal user(Principal user) {
//        return user;
//    }
//
    @RequestMapping("/resource")
    public Map<String,Object> res() {
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public Map<String, Object> authenticate(@RequestBody Account account) {

        Map<String, Object> response = new HashMap<>();

        if(null != accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())){
            response.put("response", "OK");
            request.getSession().setAttribute("user", account.getUsername());
        }
        else response.put("response", "NO");

        return response;
    }

    @RequestMapping(value = "/registerAccount", method = RequestMethod.POST)
    public Map<String, Object> register(@RequestBody Account account) {

        Map<String, Object> response = new HashMap<>();

        if(null != accountRepository.findByUsername(account.getUsername())){
            log.info("NO " + account.getUsername() + " " + account.getPassword());
            response.put("response", "NO");
        }
        else {
            log.info("YES " + account.getUsername() + " " + account.getPassword());
            response.put("response", "YES");
            accountRepository.save(account);
        }

        return response;
    }

//    @RequestMapping("/")
//    public String home(){
//        return "Home";
//    }

    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(AccountRepository repository) {
        return (args) -> {
            // save a couple of accounts
            repository.save(new Account("user", "pass"));
            repository.save(new Account("username", "password"));
        };
    }



//    @Configuration
//    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .httpBasic().and()
//                    .authorizeRequests()
//                    .antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll().anyRequest()
//                    .authenticated().and()
//                    .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
//                    .csrf().csrfTokenRepository(csrfTokenRepository());
//        }
//
//        private CsrfTokenRepository csrfTokenRepository() {
//            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//            repository.setHeaderName("X-XSRF-TOKEN");
//            return repository;
//        }
//    }

}
