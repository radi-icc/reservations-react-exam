package com.pid.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDto {

    @NotNull(message = "Representation id is required")
    private Long representationId;

    @NotNull(message = "Price id is required")
    private Long priceId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotBlank(message = "Ticket delivery method is required")
    @Pattern(regexp = "EMAIL|PICKUP", message = "Ticket delivery method must be EMAIL or PICKUP")
    private String ticketDeliveryMethod;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "CARD|ONSITE", message = "Payment method must be CARD or ONSITE")
    private String paymentMethod;
}
