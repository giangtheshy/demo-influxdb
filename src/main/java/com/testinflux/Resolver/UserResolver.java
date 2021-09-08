package com.testinflux.Resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver, GraphQLSubscriptionResolver {
  private static List<User> database = new ArrayList<>();
  private ConcurrentHashMap<UUID, FluxSink<User>> subscribers = new ConcurrentHashMap<>();

  @Bean
  public void init() {
    database.add(User.builder().id(UUID.randomUUID()).username("giang").password("12345").build());
    database.add(User.builder().id(UUID.randomUUID()).username("lam").password("12345").build());
    database.add(User.builder().id(UUID.randomUUID()).username("loc").password("12345").build());
    database.add(User.builder().id(UUID.randomUUID()).username("an").password("12345").build());
    database.add(User.builder().id(UUID.randomUUID()).username("thuyen").password("12345").build());
    database.add(User.builder().id(UUID.randomUUID()).username("khanh").password("12345").build());
  }

  public User queryUser(UUID id) {
    return database.stream()
        .filter(user -> user.getId().equals(id))
        .collect(Collectors.toList())
        .get(0);
  }

  public List<User> getUsers() {

    return database;
  }

  public List<User> updateUser(UUID id, String name) {
    List<User> collect =
        database.stream()
            .peek(
                user -> {
                  if (user.getId().equals(id)) {
                    user.setUsername(name);
                  }
                })
            .collect(Collectors.toList());
    return collect;
  }

  public Publisher<User> onUserUpdate(UUID id) {
    return Flux.create(
        subscriber ->
            subscribers.put(id, subscriber.onDispose(() -> subscribers.remove(id, subscriber))),
        FluxSink.OverflowStrategy.LATEST);
  }
}
