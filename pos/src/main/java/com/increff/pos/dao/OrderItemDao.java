package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


@Repository
public class OrderItemDao extends AbstractDao {

    private static final String SELECT_ID = "select orderItemPojo from OrderItemPojo orderItemPojo where id=:id";
    private static final String SELECT_ALL_BY_ORDER_ID = "select orderItemPojo from OrderItemPojo orderItemPojo where orderId=:orderId";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        em.persist(orderItemPojo);
    }

    public OrderItemPojo select(Integer id) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ID, OrderItemPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderItemPojo> selectAll(Integer orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ALL_BY_ORDER_ID, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }


}
