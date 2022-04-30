package com.polarbookshop.orderservice.order.domain;

import com.polarbookshop.orderservice.book.Book;
import com.polarbookshop.orderservice.book.BookClient;
import com.polarbookshop.orderservice.order.event.OrderAcceptedMessage;
import com.polarbookshop.orderservice.order.event.OrderDispatchedMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final BookClient bookClient;
    private final StreamBridge streamBridge;

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Flux<Order> consumeOrderDispatchedEvent(Flux<OrderDispatchedMessage> flux) {
        return flux
                .flatMap(message -> orderRepository.findById(message.getOrderId()))
                .map(this::buildDispatchedOrder)
                .flatMap(orderRepository::save);
    }

    public Mono<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    //TODO it seems that this doesn't work perfect, is should be fixed later
//    @Transactional
    public Mono<Order> submitOrder(String isbn, int quantity) {
        return bookClient.getBookByIsbn(isbn)
                .flatMap(book -> Mono.just(buildAcceptedOrder(book, quantity)))
                .defaultIfEmpty(buildRejectedOrder(isbn, quantity))
                .flatMap(orderRepository::save)
                .doOnNext(this::publishOrderAcceptedEvent);
    }

    public static Order buildRejectedOrder(String isbn, int quantity) {
        return new Order(isbn, quantity, OrderStatus.REJECTED);
    }

    public static Order buildAcceptedOrder(Book book, int quantity) {
        return new Order(book.getIsbn(), book.getAuthor() + "---" + book.getTitle(),
                book.getPrice(),
                quantity,
                OrderStatus.ACCEPTED);
    }

    private Order buildDispatchedOrder(Order existingOrder) {
        return Order.builder()
                .id(existingOrder.getId())
                .bookIsbn(existingOrder.getBookIsbn())
                .bookName(existingOrder.getBookName())
                .bookPrice(existingOrder.getBookPrice())
                .quantity(existingOrder.getQuantity())
                .status(OrderStatus.DISPATCHED)
                .createdDate(existingOrder.getCreatedDate())
                .lastModifiedDate(existingOrder.getLastModifiedDate())
                .version(existingOrder.getVersion())
                .build();
    }

    private void publishOrderAcceptedEvent(Order order) {
        if (!order.getStatus().equals(OrderStatus.ACCEPTED)) {
            return;
        }

        OrderAcceptedMessage orderAcceptedMessage = new OrderAcceptedMessage(order.getId());
        log.info("Sending order accepted event with id: {}", order.getId());
        boolean result = streamBridge.send("acceptOrder-out-0", orderAcceptedMessage);
        log.info("Result of sending data for order with id {}: {}", order.getId(), result);
    }
}
