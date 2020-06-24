package net.peihuan.blogapi.web.controller;

import net.peihuan.blogapi.form.LoginForm;
import net.peihuan.blogapi.service.UserService;
import net.peihuan.blogapi.vo.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping(value = "login", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ResponseEntity<RestResult>> login(@Valid LoginForm form, BindingResult bindingResult) {
        return userService.login(form);
    }


    @PostMapping(value = "logout", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ResponseEntity<RestResult>> logout() {
        return userService.logout();
    }


}
