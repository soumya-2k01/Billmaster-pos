package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderDao dao;

    public void add(OrderPojo orderPojo){
        dao.insert(orderPojo);
    }

    public OrderPojo get(Integer id){
        return dao.select(id);
    }

    public List<OrderPojo> getAllByDate(String startDate, String endDate) {
        return dao.selectAllByDate(startDate, endDate);
    }

    public List<OrderPojo> getAll() {
        return dao.selectAll();
    }

    private OrderPojo getCheck(Integer id) throws ApiException {
        OrderPojo orderPojo = get(id);
        if (orderPojo == null) {
            throw new ApiException("No order exists with the given id " + id);
        }
        return orderPojo;
    }


}
