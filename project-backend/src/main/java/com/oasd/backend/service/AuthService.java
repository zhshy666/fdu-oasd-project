package com.oasd.backend.service;

import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.repository.TravelUserRepo;
import com.oasd.backend.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (usernameOrEmail.contains("@")){
            return travelUserRepo.findUserByEmailAndPass(usernameOrEmail, psw);
        }
        return travelUserRepo.findUserByUsernameAndPass(usernameOrEmail, psw);
    }

    public String register(String username, String password, String email){
        List<TravelUser> userList = travelUserRepo.findUserByUsername(username);
        if (userList.size()!=0){
            return "username";
        }
        boolean isEmailExists = travelUserRepo.findUserByEmail(email);
        if (isEmailExists){
            return "email";
        }
        String psw = null;
        try {
            psw = AESUtil.desEncrypt(password).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        travelUserRepo.insertUser(username, psw, email);
        return "success";
    }
}