package com.dca.dcaapi.resources;

import com.dca.dcaapi.Constants;
import com.dca.dcaapi.domain.User;
import com.dca.dcaapi.services.UserServices;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    UserServices userServices;
    @PostMapping("/signUp")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String,Object> userMap){
        String username = (String) userMap.get("username");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        User user = userServices.registerUser(username, email, password);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap){
        String email = (String)userMap.get("email");
        String password = (String)userMap.get("password");
        User user = userServices.validateUser(email, password);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }
    private Map<String, String> generateJWTToken(User user){
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("user_id" , user.getUserId())
                .claim("username" , user.getUsername())
                .claim("email" , user.getEmail())
                .claim("password" , user.getPassword())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
