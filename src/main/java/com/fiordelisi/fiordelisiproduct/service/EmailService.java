package com.fiordelisi.fiordelisiproduct.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.staff.email}")
    private String staffEmail;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendNewOrderNotification(String customerName, double total) {
        String subject = "🔔 Đơn hàng mới từ " + customerName;
        String text = String.format("""
                Xin chào staff,

                Có một đơn hàng mới vừa được tạo:
                - Khách hàng: %s
                - Tổng tiền: %,.0f₫

                Vui lòng kiểm tra hệ thống để xử lý đơn hàng.

                -- Hệ thống Fiordelisi
                """, customerName, total);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(staffEmail);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("✅ Email thông báo đơn hàng mới đã gửi tới {}", staffEmail);
        } catch (Exception e) {
            log.error("❌ Lỗi khi gửi email thông báo: {}", e.getMessage());
        }
    }
}
