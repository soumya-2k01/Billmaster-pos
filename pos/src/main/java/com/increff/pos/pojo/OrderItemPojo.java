package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class OrderItemPojo {
    @Id
    @SequenceGenerator(name = "seq", initialValue = 100001, allocationSize = 1)
    @GeneratedValue(generator = "seq")
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Double sellingPrice;


}
