package com.eicon.demo.services.enums;

import java.math.BigDecimal;

public enum DiscountType {
    NO_DISCOUNT(BigDecimal.ONE),
    DISCOUNT_5_PERCENT(new BigDecimal("0.95")),
    DISCOUNT_10_PERCENT(new BigDecimal("0.9"));

    private final BigDecimal multiplier;

    DiscountType(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }


}
