package com.polarbookshop.orderservice.order.web;

import javax.validation.Valid;

import com.polarbookshop.orderservice.order.domain.Order;
import com.polarbookshop.orderservice.order.domain.OrderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public Flux<Order> getAllOrders() {
		return orderService.getAllOrders();
	}

	@GetMapping("{id}")
	public Mono<Order> getOrderById(@PathVariable Long id) {
		return orderService.getOrder(id);
	}

	@PostMapping
	public Mono<Order> submitOrder(@RequestBody @Valid OrderRequest orderRequest) {
		return orderService.submitOrder(orderRequest.getIsbn(), orderRequest.getQuantity());
	}
}
