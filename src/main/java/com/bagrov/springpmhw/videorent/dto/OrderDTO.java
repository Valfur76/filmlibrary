package com.bagrov.springpmhw.videorent.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderDTO {

    private int userId;
    private int filmId;
    private LocalDateTime rentDate;
    private int rentPeriod;
    private boolean purchase;
}
