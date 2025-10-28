package com.fiordelisi.fiordelisiproduct.service.ServiceImpl;

import com.fiordelisi.fiordelisiproduct.dto.OrderDto;
import com.fiordelisi.fiordelisiproduct.entity.Order;
import com.fiordelisi.fiordelisiproduct.repository.OrderRepository;
import com.fiordelisi.fiordelisiproduct.service.EmailService;
import com.fiordelisi.fiordelisiproduct.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRespository;
    private final EmailService emailService;

    @Override
    public Optional<Order> findById(String id) {
        return orderRespository.findById(id);
    }

    @Override
    public Order create(Order order) {
        order.setId(null);
        return orderRespository.save(order);
    }

    @Override
    public Optional<Order> update(String id, OrderDto dto) {
        return orderRespository.findById(id).map(order -> {
            order.setStatus(dto.getStatus());
            return orderRespository.save(order);
        });
    }

    @Override
    public Order saveFromDto(OrderDto dto) {
        Order order = Order.builder()
                .customer(dto.getCustomer())
                .items(dto.getItems())
                .subtotal(dto.getSubtotal())
                .shipping(dto.getShipping())
                .total(dto.getTotal())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRespository.save(order);

        emailService.sendNewOrderNotification(
                dto.getCustomer().getFullName(),
                dto.getTotal()
        );

        return savedOrder;
    }

    @Override
    public void delete(String id) {
        orderRespository.deleteById(id);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRespository.findAll(pageable);
    }

    @Override
    public Page<Order> findByCustomerEmail(String email, Pageable pageable) {
        if (email == null) {
            List<Order> orders = orderRespository.findAll();
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), orders.size());
            List<Order> subList = orders.subList(start, end);
            return new PageImpl<>(subList, pageable, orders.size());
        }
        return orderRespository.findByCustomer_EmailContaining(email, pageable);
    }
}
