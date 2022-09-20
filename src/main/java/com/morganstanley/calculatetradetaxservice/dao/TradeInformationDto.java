package com.morganstanley.calculatetradetaxservice.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "TRADE_INFORMATION")
@NoArgsConstructor
@AllArgsConstructor
public class TradeInformationDto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "trade_id", nullable = false)
    private Long tradeId;

    @JsonProperty(value = "business_name")
    @Column(name = "business_name",nullable = false)
    private @NotNull String businessName;

    @JsonProperty(value = "share_price_each")
    @Column(name = "share_price_each" , nullable = false)
    @Min(value = 0L, message = "The value must be positive or zero")
    private Double sharePriceEach;

    @JsonProperty(value = "total_no_of_shares_purchased")
    @Min(value = 1L, message = "The value must be positive and not zero")
    @Column(name = "total_no_of_shares_purchased", nullable = false)
    private @NotNull Long totalNoOfSharesPurchased;

    @JsonProperty(value = "tax_rate")
    @Column(name = "tax_rate_percentage", nullable = false)
    private @NotNull Double taxRate;

    @Column(name = "DT_CREATED")
    private LocalDateTime localDateTime= LocalDateTime.now();

}
