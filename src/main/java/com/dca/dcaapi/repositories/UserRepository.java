package com.dca.dcaapi.repositories;

import com.dca.dcaapi.domain.User;
import com.dca.dcaapi.exceptions.DcaAuthException;

public interface UserRepository {
    Integer create(String username, String email, String password) throws DcaAuthException;
    User findByEmailAndPassword(String email, String password) throws DcaAuthException;

    Integer getCountByEmail(String email);
    Integer getCountByUsername(String username);
    User findById(Integer userId);
}
