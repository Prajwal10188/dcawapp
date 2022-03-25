package com.dca.dcaapi.repositories;

import com.dca.dcaapi.domain.User;
import com.dca.dcaapi.exceptions.DcaAuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import static java.sql.Types.VARCHAR;

@Repository

public class UserRepositoryImpl implements UserRepository{

    private static final String SQL_CREATE = "INSERT INTO USERS(USER_ID, USERNAME, EMAIL, PASSWORD) VALUES(NEXTVAL('USERS_SEQ'), ?, ?, ?)";
    private static final String SQL_FIND_BY_EMAILPASS = "SELECT * FROM USERS WHERE EMAIL = ?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM USERS WHERE USER_ID = ?";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM USERS WHERE EMAIL = ?";
    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(*) FROM USERS WHERE USERNAME = ?";
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Integer create(String username, String email, String password) throws DcaAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, hashedPassword);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("user_id");
        }catch(Exception e){
            throw new DcaAuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws DcaAuthException {
        try{
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAILPASS, userRowMapper, new Object[]{email});
            if(!BCrypt.checkpw(password, user.getPassword()))
                throw new DcaAuthException("Invalid Email/Password");
            return user;
        }catch(EmptyResultDataAccessException e){
            throw new DcaAuthException("Invalid Mobile Number/Password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        int[] arr = {VARCHAR};
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, arr, Integer.class);
    }

    @Override
    public Integer getCountByUsername(String username) {
        int[] arr = {VARCHAR};
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_USERNAME, new Object[]{username}, arr, Integer.class);
    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, new Object[]{userId});
    }
    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password")
        );
    });
}
