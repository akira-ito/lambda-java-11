package com.zap.api.domain.property;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class PricingInfos{
	private BigDecimal yearlyIptu;
	private BigDecimal price;
    private BigDecimal rentalTotalPrice;
    private BusinessType businessType;
    private BigDecimal monthlyCondoFee;
    private PeriodType period;
}
