package com.increff.pos.dao;

import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;


@Repository
public class DailyReportDao extends AbstractDao {

    private static final String SELECT_ALL = "select dailyReportPojo from DailyReportPojo dailyReportPojo";
    private static final String SELECT_ALL_BY_DATE = "select dailyReportPojo from DailyReportPojo dailyReportPojo where date>=:start and date<=:end";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(DailyReportPojo p) {
        em.persist(p);
    }

    public List<DailyReportPojo> selectAllByDate(ZonedDateTime startDate, ZonedDateTime endDate) {
        TypedQuery<DailyReportPojo> query = getQuery(SELECT_ALL_BY_DATE, DailyReportPojo.class);
        query.setParameter("start", startDate);
        query.setParameter("end", endDate);
        return query.getResultList();
    }

    public List<DailyReportPojo> selectAll() {
        TypedQuery<DailyReportPojo> query = getQuery(SELECT_ALL, DailyReportPojo.class);
        return query.getResultList();
    }


}
