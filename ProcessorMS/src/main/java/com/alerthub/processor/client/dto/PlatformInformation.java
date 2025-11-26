package com.alerthub.processor.client.dto;


import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PlatformInformation {

    private Long id;
    private LocalDateTime timestamp;
    private String ownerId;
    private String project;
    private String tag;
    private String label;
    private String developerId;
    private String taskNumber;
    private String environment;
    private String userStory;
    private int taskPoint;
    private String sprint;
}

