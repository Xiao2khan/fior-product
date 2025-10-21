package com.fiordelisi.fiordelisiproduct.service;

import com.fiordelisi.fiordelisiproduct.dto.OrderDto;
import com.fiordelisi.fiordelisiproduct.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Optional<Order> findById(String id);
    Order saveFromDto(OrderDto dto);
    Order create(Order order);
    Page<Order> findAll(Pageable pageable);
    void delete(String id);
    Page<Order> findByCustomerEmail(String email, Pageable pageable);
    Optional<Order> update(String id,OrderDto dto);
}

