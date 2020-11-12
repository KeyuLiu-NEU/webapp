package com.cloud.service;


import com.cloud.domain.UserAccount;
import com.cloud.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.timgroup.statsd.StatsDClient;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserAccount saveWithEncoder(UserAccount userAccount) {
//        Long startTime=System.nanoTime();
//        try {
//            return userRepository.save(userAccount);
//        }finally {
//            long endTime = System.nanoTime();
//            long duration = (endTime - startTime);
//            statsDClient.recordExecutionTime("userSaveQuery", duration / 1000000);
//        }


        userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
        return userRepository.save(userAccount);

    }


    public UserAccount update(UserAccount userAccount){

       return userRepository.save(userAccount);
    }


    private String hashPassword(String password) {

        String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt());

        return pw_hash;


    }

    public boolean emailVaildation(String email){
        Pattern ptr = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$");
        return ptr.matcher(email).matches() ? true:false;
    }

    public boolean pwdValidation(String pwd){
        List<Rule> rules = new ArrayList<>();
        //Rule 1: Password length should be in between
        //8 and 16 characters
        rules.add(new LengthRule(9, 16));
        //Rule 2: No whitespace allowed
        rules.add(new WhitespaceRule());
        //Rule 3.a: At least one Upper-case character
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        //Rule 3.b: At least one Lower-case character
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        //Rule 3.c: At least one digit
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        //Rule 3.d: At least one special character
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1));
        PasswordValidator validator = new PasswordValidator(rules);
        PasswordData password = new PasswordData(pwd);
        RuleResult result = validator.validate(password);
        return result.isValid();
    }

    public UserAccount findByEmail(String email) {

        return userRepository.findByEmailAddress(email);
    }

    public boolean CheckIfEmailExists(String email) {
        UserAccount user = findByEmail(email);

        if (user != null) {
            return true;
        } else {

            return false;
        }
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserAccount userAccount = findByEmail(userName);
        if (userAccount == null) {

            throw new UsernameNotFoundException(userName);
        }
        return new UserDetailsImpl(userAccount);
    }
}
