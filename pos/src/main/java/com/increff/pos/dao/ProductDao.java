package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


@Repository
public class ProductDao extends AbstractDao {

    private static final String SELECT_ID = "select productPojo from ProductPojo productPojo where id=:id";
    private static final String SELECT_ALL = "select productPojo from ProductPojo productPojo";
    private static final String SELECT_ID_BY_BARCODE = "select productPojo from ProductPojo productPojo where barcode=:barcode";
    private static final String SELECT_ALL_BY_BRAND_CATEGORY_ID = "select productPojo from ProductPojo productPojo where brandCategory=:brandCategory";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(ProductPojo productPojo) {
        em.persist(productPojo);
    }

    public ProductPojo select(Integer id) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ID, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public ProductPojo select(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ID_BY_BARCODE, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
        return query.getResultList();
    }

    public List<ProductPojo> selectAll(Integer id) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL_BY_BRAND_CATEGORY_ID, ProductPojo.class);
        query.setParameter("brandCategory", id);
        return query.getResultList();
    }


}
