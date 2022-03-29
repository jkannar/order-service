package com.polarbookshop.orderservice.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	public Flux<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Mono<Order> getOrder(Long id) {
		return orderRepository.findById(id);
	}

	public Mono<Order> submitOrder(String isbn, int quantity) {
		return Mono.just(buildRejectedOrder(isbn, quantity)).flatMap(orderRepository::save);
	}

	private Order buildRejectedOrder(String isbn, int quantity) {
		return new Order(isbn, quantity, OrderStatus.REJECTED);
	}
}
