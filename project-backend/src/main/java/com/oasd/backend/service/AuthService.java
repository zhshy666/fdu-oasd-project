package com.oasd.backend.service;

import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.repository.TravelUserRepo;
import com.oasd.backend.util.AESUtil;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
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
        TravelUser user = travelUserRepo.findUserByUsername(username);
        if (user!=null){
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

    public List<TravelUser> findUserLikeUsername(String username) {
        List<TravelUser> userList = travelUserRepo.findUserLikeUsername(username);
        TravelUser travelUser = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TravelUser> delete = new LinkedList<>();
        for(TravelUser user: userList){
            if(user.getId() == travelUser.getId())
                delete.add(travelUser);
        }
        userList.removeAll(delete);
        return userList;
    }

    public TravelUser findUserById(int id) {
        return travelUserRepo.findUserById(id);
    }

    public TravelUser findUserByUsername(String username) {
        return travelUserRepo.findUserByUsername(username);
    }

}