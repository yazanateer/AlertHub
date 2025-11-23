package com.alerthub.evaluation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "platformInformation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // synthetic PK

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "owner_id")
    private Long ownerId;  // manager_id

    private String project;

    private String tag;

    @Column(nullable = true)
    private String label;

    @Column(name = "developer_id")
    private Long developerId;

    @Column(name = "task_number")
    private String taskNumber;

    private String environment;

    @Column(name = "user_story")
    private String userStory;

    @Column(name = "task_point")
    private Integer taskPoint;

    private String sprint;
}
