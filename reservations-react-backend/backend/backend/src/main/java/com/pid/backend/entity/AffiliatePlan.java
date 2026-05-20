package com.pid.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "affiliate_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffiliatePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "api_limit")
    private Integer apiLimit;

    @Column(name = "monthly_price")
    private BigDecimal monthlyPrice;
}