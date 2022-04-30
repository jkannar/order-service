package com.polarbookshop.orderservice.order.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDispatchedMessage {

	@JsonProperty("orderid")
	private Long orderId;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OrderDispatchedMessage) {
			return orderId.equals(((OrderDispatchedMessage) obj).getOrderId());
		} else {
			return false;
		}
	}
}
