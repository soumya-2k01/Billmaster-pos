package com.increff.fop.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceLineItem {
    private Integer sno;
    private String barcode;
    private String name;
    private Integer quantity;
    private Double sellingPrice;
    private Double total;

    public Double getTotal() {
        return quantity * sellingPrice;
    }

}
