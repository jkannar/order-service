package com.polarbookshop.orderservice.order.domain;

import com.polarbookshop.orderservice.book.Book;
import com.polarbookshop.orderservice.book.BookClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookClient bookClient;

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public Mono<Order> submitOrder(String isbn, int quantity) {
        return bookClient.getBookByIsbn(isbn)
                .flatMap(book -> Mono.just(buildAcceptedOrder(book, quantity)))
                .defaultIfEmpty(buildRejectedOrder(isbn, quantity))
                .flatMap(orderRepository::save);
    }

    private Order buildRejectedOrder(String isbn, int quantity) {
        return new Order(isbn, quantity, OrderStatus.REJECTED);
    }

    private Order buildAcceptedOrder(Book book, int quantity) {
        return new Order(book.getIsbn(), book.getAuthor() + "---" + book.getTitle(),
                book.getPrice(),
                quantity,
                OrderStatus.ACCEPTED);
    }
}
