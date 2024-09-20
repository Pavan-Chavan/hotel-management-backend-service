package com.teams.service;

import com.teams.entity.SubUser;
import com.teams.exception.HotelManagementException;
import com.teams.entity.Login;
import com.teams.repository.LoginRepository;
import com.teams.repository.ManagementUserRepository;
import com.teams.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author dgardi
 */
@Service
@Slf4j
public class LoginService implements UserDetailsService {
    @Autowired
    private LoginRepository loginRepository;
//
//    @Autowired
//    AuthenticationManager authenticationManager;

    @Autowired
    ManagementUserRepository managementUserRepository;

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public Login saveLoginCredentials(String username, String password) {
        try{
            if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                log.info("Invalid Data provided for username {} password {}",username,password);
                throw new IllegalArgumentException("Invalid Data Provided");
            }
            Login loginDetails = new Login();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            Optional<Login> existingLoginDetails = loginRepository.findById(username);
            if(existingLoginDetails.isPresent()) {
                log.info("login details are already present for username {} so updating the password",username);
                loginDetails = existingLoginDetails.get();
                loginDetails.setPassword(encoder.encode(password));
            } else {
                log.info("Saving the login credentials with username {}",username);
                loginDetails.setUsername(username);
                loginDetails.setPassword(encoder.encode(password));
            }
            loginRepository.save(loginDetails);
            return loginDetails;
        } catch(IllegalArgumentException iae){
            log.error("Invalid details provided ",iae);
            throw iae;
        } catch (Exception e){
            log.error("Error occurred while saving the login credentials ",e);
            throw new HotelManagementException(e.getMessage(),e);
        }
    }

//    public Login login(Login login) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
//        return login;
//    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login user = loginRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        SubUser subUser = managementUserRepository.findSubUserByLoginUsername(username);
        return UserDetailsImpl.build(user,subUser);
    }
}
