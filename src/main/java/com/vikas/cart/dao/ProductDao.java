package com.vikas.cart.dao;

import com.vikas.cart.model.Product;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProductDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List getProductDetails() {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Product.class);
        return criteria.list();
    }
}
