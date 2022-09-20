package com.morganstanley.calculatetradetaxservice.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AddedTradeDetailsResponse {

    private Long trade_id;
    private String status;
    private String message;
    private LocalDateTime timeStamp;
}
