package com.example.beloginauth2.Controller;

import com.example.beloginauth2.Model.Role;
import com.example.beloginauth2.Model.User;
import com.example.beloginauth2.Service.RoleService;
import com.example.beloginauth2.Service.TokenService;
import com.example.beloginauth2.Service.UserService;
import com.example.beloginauth2.dto.JwtLogin;
import com.example.beloginauth2.dto.LoginResponse;
import com.example.beloginauth2.dto.TokenDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


// http://localhost:8080
@RestController
@RequestMapping("/social")
@CrossOrigin("*")
//http://localhost:8080/social
public class SocialController {

    private UserService userService;

    private RoleService roleService;

    private TokenService tokenService;

    private PasswordEncoder passwordEncoder;

    private String email;


    @Value("${google.id}")
    private String idClient;

    @Value("${mySecret.password}")
    private String password;

    @Autowired
    public SocialController(UserService userService,RoleService roleService,TokenService tokenService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    //http://localhost:8080/social/google - api google
    @PostMapping("/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestBody TokenDto tokenDto) throws Exception {

        NetHttpTransport transport = new NetHttpTransport();
        // ????y l?? ???????ng d???n th?? m???c s??? l??u t???o file x??c th???c khi b???n ????ng nh???p l???n ?????u ti??n
        JacksonFactory factory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder ver =
                new GoogleIdTokenVerifier.Builder(transport,factory)
                        .setAudience(Collections.singleton(idClient));
        GoogleIdToken googleIdToken = GoogleIdToken.parse(ver.getJsonFactory(),tokenDto.getToken());
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
//        return new ResponseEntity<>(payload,HttpStatus.OK);}
//        L???y v??? th??ng tin email t??? api c???a gg

        email = payload.getEmail();
        User user = new User();
//Khi ????ng nh???p v??o, n???u ???? ????ng nh???p r???i th?? n?? cho v??o lu??n, ch??a ????ng nh???p n?? s??? g???i h??m t???o user m???i
        if(userService.ifEmailExist(email)){
            user = userService.getUserByMail(email);
        } else {
            user = createUser(email);
        }
        ///////////////////////////
        JwtLogin jwtLogin = new JwtLogin();
        jwtLogin.setEmail(user.getEmail());
        jwtLogin.setPassword(password);
        ///////////////////////////

        return new ResponseEntity<LoginResponse>(tokenService.login(jwtLogin), HttpStatus.OK);
    }

    private User createUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        List<Role> roles = roleService.getRoles();
        user.getRoles().add(roles.get(0));
        return userService.saveUser(user);
    }


}
