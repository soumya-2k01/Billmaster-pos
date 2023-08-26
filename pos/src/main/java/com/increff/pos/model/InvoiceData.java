package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceData {
    private String invoiceNumber;
    private String invoiceDate;
    private String invoiceTime;
    private Double total;
    private List<InvoiceLineItem> lineItems;

    public Double getTotal() {
        total = 0.0;
        for (InvoiceLineItem item : lineItems) {
            total += item.getTotal();
        }
        return total;
    }
}
