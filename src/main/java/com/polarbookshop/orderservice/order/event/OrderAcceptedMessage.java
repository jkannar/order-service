package com.polarbookshop.orderservice.order.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAcceptedMessage {

	@JsonProperty("orderid")
	private Long orderId;
}


