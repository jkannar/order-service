package com.polarbookshop.orderservice.order.domain;

import com.polarbookshop.orderservice.order.persistence.PersistableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table("orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends PersistableEntity {

	private String bookIsbn;
	private String bookName;
	private Double bookPrice;
	private Integer quantity;
	private OrderStatus status;

	public  Order(String bookIsbn, int quantity, OrderStatus status) {
		this.bookIsbn = bookIsbn;
		this.quantity = quantity;
		this.status = status;
	}

	@Builder
	private Order(Long id, Timestamp createdDate, Timestamp lastModifiedDate, int version,
				  String bookIsbn, String bookName, Double bookPrice, Integer quantity,
				  OrderStatus status) {
		super(id, createdDate, lastModifiedDate, version);
		this.bookIsbn = bookIsbn;
		this.bookName = bookName;
		this.bookPrice = bookPrice;
		this.quantity = quantity;
		this.status = status;
	}
}

