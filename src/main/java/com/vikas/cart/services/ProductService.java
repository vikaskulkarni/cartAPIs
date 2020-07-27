package com.vikas.cart.services;

import com.vikas.cart.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public List getProductDetails() {
        return productDao.getProductDetails();
    }

}
