package com.polarbookshop.orderservice.order.web;

import com.polarbookshop.orderservice.order.domain.Order;
import com.polarbookshop.orderservice.order.domain.OrderStatus;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class OrderJsonTests {

    @Autowired
    private JacksonTester<Order> json;

    @Test
    void testSerialize() throws Exception {
        Order order = new Order("1234567890", "Book Name", 9.90, 1, OrderStatus.ACCEPTED);
        JsonContent<Order> jsonContent = json.write(order);
        assertThat(jsonContent).extractingJsonPathStringValue("@.bookIsbn")
                .isEqualTo(order.getBookIsbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.bookName")
                .isEqualTo(order.getBookName());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.bookPrice")
                .isEqualTo(order.getBookPrice());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.quantity")
                .isEqualTo(order.getQuantity());
        assertThat(jsonContent).extractingJsonPathStringValue("@.status")
                .isEqualTo(order.getStatus().toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version")
                .isEqualTo(order.getVersion());
    }
}
