package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao dao;

    @Transactional
    public void addAll(List<OrderItemPojo> orderItemPojos) {
        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            dao.insert(orderItemPojo);
        }
    }

    //Get all items by orderId
    public List<OrderItemPojo> getAll(Integer id) {
        return dao.selectAll(id);
    }


}
