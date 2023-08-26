package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


@Repository
public class OrderDao extends AbstractDao {

    private static final String DELETE_ID = "delete from OrderPojo orderPojo where id=:id";
    private static final String SELECT_BY_ID = "select orderPojo from OrderPojo orderPojo where id=:id";
    private static final String SELECT_BY_TIME = "select orderPojo from OrderPojo orderPojo where time=:time";
    private static final String SELECT_ALL = "select orderPojo from OrderPojo orderPojo";
    private static final String SELECT_ALL_BY_DATE = "select orderPojo from OrderPojo orderPojo where time>=:start and time<=:end";


    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(OrderPojo p) {
        em.persist(p);
    }

    public void delete(Integer id) {
        Query query = em.createQuery(DELETE_ID);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public OrderPojo select(String time) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_BY_TIME, OrderPojo.class);
        query.setParameter("time", time);
        return getSingle(query);
    }

    public OrderPojo select(Integer id) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_BY_ID, OrderPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);
        return query.getResultList();
    }

    public List<OrderPojo> selectAllByDate(String startDate, String endDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL_BY_DATE, OrderPojo.class);
        query.setParameter("start", startDate);
        query.setParameter("end", endDate);
        return query.getResultList();
    }


}
