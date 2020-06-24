package net.peihuan.blogapi.web.filter;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.peihuan.blogapi.service.UserService;
import net.peihuan.blogapi.vo.RestResult;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.peihuan.blogapi.vo.ResponseAPIStatus.UNAUTHORIZED;

@Configuration
@Slf4j
public class TokenFilter implements WebFilter {

    @Autowired
    private UserService userService;

    private List<String> releaseUrl = Lists.newArrayList("/user/login/", "/file/upload/");

    private Gson gson = new Gson();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        if (releaseUrl.contains(request.getPath().toString())) {
            return chain.filter(exchange);
        }
        String token = Optional.ofNullable(request.getHeaders().getFirst("Authorization")).orElse("");

        //if (!token.equals(userService.getAuthToken())) {
        //    return unAuthorized(exchange.getResponse(), "认证失败");
        //}
        //
        //if (userService.getLastActiveTime().plusHours(1).isBeforeNow()) {
        //    return unAuthorized(exchange.getResponse(), "认证过期");
        //}

        userService.updateActiveTime();

        return chain.filter(exchange);
    }



    protected Mono<Void> unAuthorized(ServerHttpResponse response, String message){
        String json = gson.toJson(new RestResult(UNAUTHORIZED, message));
        response.setStatusCode(UNAUTHORIZED.getHttpStatus());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(json.getBytes())));

    }

}
