package com.polarbookshop.orderservice.order.domain;

import com.polarbookshop.orderservice.order.persistence.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataR2dbcTest
@Import(DataConfig.class)
@Testcontainers
class OrderRepositoryR2dbcTests {

    @Container
    static PostgreSQLContainer<?> postgresql;

    static {
        postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.4"))
                .withDatabaseName("polardb_order")
                .withUsername("postgres")
                .withPassword("Miasanmia123");
        postgresql.withInitScript("db/migration/V1__Initial_schema.sql");
        postgresql.start();
    }

    @Autowired
    private OrderRepository orderRepository;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", OrderRepositoryR2dbcTests::r2dbcUrl);
        registry.add("spring.r2dbc.username", postgresql::getUsername);
        registry.add("spring.r2dbc.password", postgresql::getPassword);
        registry.add("spring.flyway.url", postgresql::getJdbcUrl);
        registry.add("spring.flyway.user", postgresql::getUsername);
        registry.add("spring.flyway.password", postgresql::getPassword);
    }

    private static String r2dbcUrl() {
        return String.format("r2dbc:postgresql://%s:%s/%s", postgresql.getContainerIpAddress(),
                postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgresql.getDatabaseName());
    }

    @Test
    void findOrderByIdWhenNotExisting() {
        StepVerifier.create(orderRepository.findById(394L))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void createRejectedOrder() {
        String bookIsbn = "1234567890";
        Order rejectedOrder = OrderService.buildRejectedOrder(bookIsbn, 3);

        //----------------------old-faschioned--------------------------------------------------
//        orderRepository.save(rejectedOrder).block();
//        Flux<Order> orderFlux = orderRepository.findAll();
//        List<Order> orderList = orderFlux.toStream().collect(Collectors.toList());
//        assertEquals(1, orderList.size());
//        assertEquals(orderList.get(0).getBookIsbn(), bookIsbn);
//        assertEquals(orderList.get(0).getStatus(), OrderStatus.REJECTED);

        //-------------------------original-version----------------------------------------------
        StepVerifier.create(orderRepository.save(rejectedOrder))
                .expectNextMatches(order -> order.getStatus().equals(OrderStatus.REJECTED))
                .verifyComplete();
    }
}