package me.jittagornp.learning.rediscluster.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jittagornp.learning.rediscluster.model.User;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final ReactiveRedisOperations<String, User> userRedisOperations;

    private String makeKey(final String id) {
        return "user:" + id;
    }

    @GetMapping
    public Flux<User> findAll() {
        return userRedisOperations.keys(makeKey("*"))
                .flatMap(key -> {
                    log.debug("key => {}", key);
                    return userRedisOperations.opsForValue()
                            .get(key);
                });
    }

    @GetMapping("/{id}")
    public Mono<User> findById(@PathVariable("id") final String id) {
        return userRedisOperations.opsForValue()
                .get(makeKey(id))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("not found"))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> create(@RequestBody final User user) {
        final String id = UUID.randomUUID().toString();
        user.setId(id);
        return userRedisOperations.opsForValue()
                .set(makeKey(id), user)
                .thenReturn(user);
    }

    @PutMapping("/{id}")
    public Mono<User> update(@PathVariable("id") final String id, @RequestBody final User user) {
        return findById(id)
                .flatMap(dbUser -> {
                    dbUser.setUsername(user.getUsername());
                    dbUser.setName(user.getName());
                    return userRedisOperations.opsForValue()
                            .set(makeKey(id), dbUser)
                            .thenReturn(dbUser);
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable("id") final String id) {
        return findById(id)
                .flatMap(dbUser -> {
                    return userRedisOperations.opsForValue()
                            .delete(makeKey(id))
                            .then();
                });
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Flux<Void> deleteAll() {
        return userRedisOperations.keys(makeKey("*"))
                .flatMap(key -> {
                    log.debug("key => {}", key);
                    return userRedisOperations.opsForValue()
                            .delete(key)
                            .then();
                });
    }
}
