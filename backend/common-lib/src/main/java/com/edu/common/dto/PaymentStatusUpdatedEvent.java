package com.edu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatusUpdatedEvent {
    private String paymentId;
    private String tuitionId;
    private String status;
    private String studentId;
}
