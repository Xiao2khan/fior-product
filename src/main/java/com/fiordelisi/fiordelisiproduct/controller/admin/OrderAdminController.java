package com.fiordelisi.fiordelisiproduct.controller.admin;

import com.fiordelisi.fiordelisiproduct.dto.OrderDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.fiordelisi.fiordelisiproduct.controller.BaseController;
import com.fiordelisi.fiordelisiproduct.entity.Order;
import com.fiordelisi.fiordelisiproduct.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class OrderAdminController extends BaseController {
    private final OrderService orderService;

    @GetMapping({"/admin/orders"})
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> result = orderService.findByCustomerEmail(q,pageable);
        model.addAttribute("order", result);
        return "admin/order/list";
    }

    @GetMapping({"/admin/orders/edit/{id}"})
    public String edit(@PathVariable String id, Model model) {
        Optional<Order> order=  orderService.findById(id);
        model.addAttribute("order", order);
        return "admin/order/form";
    }

    @PostMapping("/admin/orders/edit/{id}")
    public String updateStatus(
            @PathVariable String id,
            @RequestParam("status") String status
    ) {
        Optional<Order> updatedOrder = orderService.update(id, OrderDto.builder().status(status).build());

        if (updatedOrder.isPresent()) {
            return "redirect:/admin/orders";
        } else {
            return "redirect:/admin/orders?error=notfound";
        }
    }

    @PostMapping({"api/orders"})
    public ResponseEntity<?> createOrder(@RequestBody OrderDto dto) {
        log.info(dto.toString());
        try {
            Order savedOrder = orderService.saveFromDto(dto);
            log.info(dto.toString());
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi lưu đơn hàng!", "error", e.getMessage()));
        }
    }

    @PostMapping("/admin/orders/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        orderService.delete(id);
        ra.addFlashAttribute("success", "Order deleted successfully");
        return "redirect:/admin/orders";
    }
}
