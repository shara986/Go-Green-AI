package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AdminChartDataResponseDto {
    private List<String> labels;
    private List<Double> values;
    private Map<String, Object> metadata;
}
