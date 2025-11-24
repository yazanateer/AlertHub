package com.alerthub.evaluation.repository.projection;

public interface DeveloperLabelCountProjection {
    Long getDeveloperId();
    String getLabel();
    Long getCount();
}
