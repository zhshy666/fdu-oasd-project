package com.oasd.backend.service;

import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.repository.TravelUserRepo;
import com.oasd.backend.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class AuthService {
    private TravelUserRepo travelUserRepo;

    @Autowired
    public AuthService(TravelUserRepo repo) {
        this.travelUserRepo = repo;
    }

    /**
     * @param usernameOrEmail username from the front end
     * @param password password from the front end
     * @return return token info to the front end(call generateToken func)
     * return value sent to : AuthController
     */
    public TravelUser login(String usernameOrEmail, String password) {
        // decode
        String psw = null;
        try {
            psw = AESUtil.desEncrypt(password).trim();
            System.out.println(psw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (usernameOrEmail.contains("@")){
            return travelUserRepo.findUserByEmailAndPass(usernameOrEmail, psw);
        }
        return travelUserRepo.findUserByUsernameAndPass(usernameOrEmail, psw);
        // Step1 : user can be found or not
//        if(user == null){
//            return null;
//        } else {
//            // Step2: username and password match or not
//            String psw = user.getPassword();
//            if(passwordEncoder.matches(password,psw)){
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                return user;
//            }else{
//                return null;
//            }
//        }
    }

    public String register(String username, String password, String email){
        boolean isUsernameExists = travelUserRepo.findUserByUsername(username);
        if (isUsernameExists){
            return "username";
        }
        boolean isEmailExists = travelUserRepo.findUserByEmail(email);
        if (isEmailExists){
            return "email";
        }
        travelUserRepo.insertUser(username, password, email);
        return "success";
    }
}