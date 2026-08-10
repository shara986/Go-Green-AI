package com.gogreen.ai.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NurseryRequestDto {

    @NotNull
    private UUID userId;

    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotBlank
    @Size(max = 500)
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 20)
    private String postalCode;

    @Email
    @Size(max = 150)
    private String contactEmail;

    @Size(max = 20)
    private String contactPhone;

    @Min(0)
    @Max(5)
    private Double rating;

    private boolean verified;

    @Size(max = 500)
    private String logoUrl;
}
