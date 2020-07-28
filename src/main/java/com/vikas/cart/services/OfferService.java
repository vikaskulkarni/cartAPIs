package com.vikas.cart.services;

import com.vikas.cart.dao.IOfferService;
import com.vikas.cart.model.OfferCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService implements IOfferService {

    @Autowired
    private JdbcTemplate dbTemplate;

    @Override
    public List<OfferCode> findAll() {
        String sql = "SELECT * FROM OFFER_CODE";

        return dbTemplate.query(sql, new BeanPropertyRowMapper<>(OfferCode.class));
    }

    @Override
    public OfferCode findById(Integer id, Class offerCode) {
        String sql = "SELECT * FROM OFFER_CODE WHERE id = ?";

        return (OfferCode) dbTemplate.queryForObject(sql, new Object[]{id},
                new BeanPropertyRowMapper<>(offerCode));
    }
}
