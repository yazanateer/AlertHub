package com.alerthub.processor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionMessage {

    private String id;
    private Integer userId;
    private String name;
    private String actionType;
    private String to;
    private String message;
    private List<List<Integer>> conditionMetricIds;
}
