package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminTopSellingPlantResponseDto {
    private String plantName;
    private long soldCount;
}
