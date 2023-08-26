package com.increff.pos.service;

import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.pojo.DailyReportPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DailyReportService {
    @Autowired
    DailyReportDao dao;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    @Scheduled(cron = "00 04 11 * * *")
    @Transactional(rollbackOn = ApiException.class)
    public void add() throws ApiException {
        ZonedDateTime date = ZonedDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = date.format(formatter);
        List<OrderPojo> orderPojos = orderService.getAllByDate(currentDate+" 00:00:00",currentDate+" 23:59:59");
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setDate(date); // Setting Date
        dailyReportPojo.setInvoicedOrdersCount(orderPojos.size()); // Setting order count
        double revenue = 0.0;
        int itemCount = 0;

        for(OrderPojo orderPojo: orderPojos){
            List<OrderItemPojo> orderItemPojos = orderItemService.getAll(orderPojo.getId());
            itemCount += orderItemPojos.size();
            revenue += orderItemPojos.stream().mapToDouble(orderItemPojo -> orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice()).sum();
        }
        dailyReportPojo.setInvoicedItemsCount(itemCount);
        dailyReportPojo.setTotalRevenue(revenue);

        dao.insert(dailyReportPojo);
    }

    public List<DailyReportPojo> getAll(){
        return dao.selectAll();
    }

    public List<DailyReportPojo> getAllByDate(ZonedDateTime startDate, ZonedDateTime endDate){
        return dao.selectAllByDate(startDate, endDate);
    }
}
