package net.peihuan.blogapi.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.peihuan.blogapi.entity.User;
import net.peihuan.blogapi.exception.AuthException;
import net.peihuan.blogapi.form.LoginForm;
import net.peihuan.blogapi.repository.UserRepository;
import net.peihuan.blogapi.vo.RestResult;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static java.lang.Thread.sleep;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Service
@Slf4j
public class UserService {

    @Getter
    private String authToken = UUID.randomUUID().toString();
    @Getter
    private DateTime lastActiveTime = DateTime.now();

    @Autowired
    private UserRepository userRepository;

    public Mono<ResponseEntity<RestResult>> login(LoginForm form) {

        String encryptedPassword = md5DigestAsHex(form.getPassword().getBytes());
        return userRepository.findByUserName(form.getUserName())
                .switchIfEmpty(Mono.error(() -> new AuthException("用户名不存在")))
                .doOnNext(user -> {
                    if (!user.getPassword().equals(encryptedPassword)) {
                        throw new AuthException("密码错误");
                    }
                    updateToken();
                    updateActiveTime();
                })
                .map(user -> ResponseEntity.ok(new RestResult("token", authToken)));
    }


    private void updateToken() {
        log.info("--------------- update token ---------------");
        authToken = UUID.randomUUID().toString().replace("-", "");
    }

    public void updateActiveTime() {
        lastActiveTime = DateTime.now();
    }


    public Mono<ResponseEntity<RestResult>> logout() {
        updateToken();
        updateActiveTime();
        return Mono.just(ResponseEntity.ok(new RestResult()));
    }

}
