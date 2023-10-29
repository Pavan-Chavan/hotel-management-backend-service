package com.teams.service;

import com.teams.exception.HotelManagementException;
import com.teams.entity.Login;
import com.teams.repository.LoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author dgardi
 */
@Service
@Slf4j
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;
    private Login login;

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public ResponseEntity saveLoginCredentials(String username, String password) {
        try{
            Optional<Login> login1 = loginRepository.findById(username);
            Login login2 = new Login();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(password);
            login2.setUsername(username);
            login2.setPassword(hashedPassword);
            if(login1.isPresent()){
                log.info("Updating the login credentials for username {}",username);
                login = loginRepository.save(login2);
                return new ResponseEntity(login.getUsername()+" updated successfully",HttpStatus.OK);
            }else{
                log.info("Saving the login credentials with username {}",username);
                login = loginRepository.save(login2);
                return new ResponseEntity(login.getUsername()+" saved successfully",HttpStatus.OK);
            }
        }catch (Exception e){
            log.error("Error occurred while saving the login credentials ",e);
            throw new HotelManagementException(e.getMessage());
        }
    }
}
