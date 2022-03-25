package com.dca.dcaapi.services;

import com.dca.dcaapi.domain.User;
import com.dca.dcaapi.exceptions.DcaAuthException;
import com.dca.dcaapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserServicesImpl implements UserServices{

    @Autowired
    UserRepository userRepository;
    @Override
    public User validateUser(String email, String password) throws DcaAuthException {
        if(email != null) email = email.toLowerCase();
        return userRepository.findByEmailAndPassword(email,password);
    }

    @Override
    public User registerUser(String username, String email, String password) throws DcaAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if(email != null) email = email.toLowerCase();
        if(!pattern.matcher(email).matches())
            throw new DcaAuthException("Invalid email format");
        Integer count1 = userRepository.getCountByEmail(email);
        Integer count2 = userRepository.getCountByUsername(username);
        if(count1 > 0)
            throw new DcaAuthException("Email already in use!");
        if(count2 > 0)
            throw new DcaAuthException("username unavailable!");
        Integer userId = userRepository.create(username, email, password);
        return userRepository.findById(userId);
    }
}
