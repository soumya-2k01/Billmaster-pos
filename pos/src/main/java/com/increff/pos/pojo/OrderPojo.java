package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@Getter
@Setter
public class OrderPojo {
    @Id
    @SequenceGenerator(name = "sequ", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(generator = "sequ")
    private Integer id;
    private String time;
    private Integer isInvoiceGenerated = 0;

}
