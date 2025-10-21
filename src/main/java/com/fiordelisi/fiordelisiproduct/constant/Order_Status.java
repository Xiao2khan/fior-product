package com.fiordelisi.fiordelisiproduct.constant;

import lombok.Getter;
@Getter
public enum Order_Status {
    PENDING("pending", "Pending"),
    REFUND("refund", "Refund"),
    SHIPPING("shipping", "Shipping"),
    SHIPPED("shipped", "Shipped"),
    COMPLETED("completed", "Completed"),
    CANCELLED("cancelled", "Cancelled"),
    DELIVERED("delivered","Delivered");


    private final String code;
    private final String displayName;

    Order_Status(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
}
