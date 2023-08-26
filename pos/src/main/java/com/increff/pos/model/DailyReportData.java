package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyReportData {
    private String date;
    private Integer invoicedOrderCount;
    private Integer invoicedItemCount;
    private Double totalRevenue;
}
