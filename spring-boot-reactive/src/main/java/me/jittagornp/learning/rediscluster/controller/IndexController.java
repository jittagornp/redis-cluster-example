package me.jittagornp.learning.rediscluster.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@RestController
public class IndexController {

    @GetMapping({"", "/"})
    public Mono<String> index() {
        return Mono.just("Redis Cluster API");
    }

}
