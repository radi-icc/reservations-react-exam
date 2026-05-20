package com.pid.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponseDto {

    private Long id;
    private Long localityId;
    private String localityName;
    private String postalCode;
    private String slug;
    private String designation;
    private String address;
    private String website;
    private String phone;
}