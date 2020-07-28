package com.vikas.cart.dao;

import com.vikas.cart.model.OfferCode;

import java.util.List;

public interface IOfferService {

    List<OfferCode> findAll();

    OfferCode findById(Integer id, Class offerCode);
}
