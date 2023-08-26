package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


@Repository
public class BrandDao extends AbstractDao {

    private static final String SELECT_ID = "select brandPojo from BrandPojo brandPojo where id=:id";
    private static final String SELECT_ALL = "select brandPojo from BrandPojo brandPojo";
    private static final String SELECT_BY_BRAND_CATEGORY = "select brandPojo from BrandPojo brandPojo where brand=:brand and category=:category";
    private static final String SELECT_BY_BRAND = "select brandPojo from BrandPojo brandPojo where brand = :brand";
    private static final String SELECT_BY_CATEGORY = "select brandPojo from BrandPojo brandPojo where category = :category";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(BrandPojo brandPojo) {
        em.persist(brandPojo);
    }

    public BrandPojo select(Integer id) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public BrandPojo select(String brand, String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND_CATEGORY, BrandPojo.class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return getSingle(query);
    }

    public List<BrandPojo> selectAllByBrandCategory(String brand, String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND_CATEGORY, BrandPojo.class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return query.getResultList();
    }

    public List<BrandPojo> selectAllByBrand(String brand) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND, BrandPojo.class);
        query.setParameter("brand", brand);
        return query.getResultList();
    }

    public List<BrandPojo> selectAllByCategory(String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_CATEGORY, BrandPojo.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
        return query.getResultList();
    }


}
