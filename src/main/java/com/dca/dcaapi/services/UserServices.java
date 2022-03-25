package com.dca.dcaapi.services;

import com.dca.dcaapi.domain.User;
import com.dca.dcaapi.exceptions.DcaAuthException;

public interface UserServices {
    User validateUser(String email, String password) throws DcaAuthException;

    User registerUser(String username, String email, String password) throws DcaAuthException;
}
