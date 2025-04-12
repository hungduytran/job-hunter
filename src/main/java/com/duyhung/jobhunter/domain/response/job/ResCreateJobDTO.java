package com.duyhung.jobhunter.domain.response.job;

import com.duyhung.jobhunter.util.constant.LevelEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ResCreateJobDTO {
    private long id;
    private String name;
    private double salary;
    private int quantity;
    private String location;
    private LevelEnum level;
    private Instant startAt;
    private Instant endDate;
    private Instant createdAt;
    private String createdBy;
    private boolean active;
    private List<String> skills;
}
