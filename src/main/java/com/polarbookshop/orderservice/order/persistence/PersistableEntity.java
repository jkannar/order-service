package com.polarbookshop.orderservice.order.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersistableEntity {

    @Id
    private Long id;

    @CreatedDate
    private Timestamp createdDate;

    @LastModifiedDate
    private Timestamp lastModifiedDate;

    @Version
    private int version;
}
