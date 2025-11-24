package com.alerthub.evaluation.repository;

import com.alerthub.evaluation.model.PlatformInformationEntity;
import com.alerthub.evaluation.repository.projection.DeveloperLabelCountProjection;
import com.alerthub.evaluation.repository.projection.LabelCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PlatformInformationRepository
        extends JpaRepository<PlatformInformationEntity, Long> {

    @Query(value = """
            SELECT developer_id AS developerId,
                   label AS label,
                   COUNT(*) AS count
            FROM platformInformation
            WHERE label = :label
              AND timestamp >= :since
            GROUP BY developer_id, label
            ORDER BY count DESC
            LIMIT 1
            """, nativeQuery = true)
    DeveloperLabelCountProjection findTopDeveloperByLabelSince(
            @Param("label") String label,
            @Param("since") LocalDateTime since);

    @Query(value = """
            SELECT label AS label,
                   COUNT(*) AS count
            FROM platformInformation
            WHERE developer_id = :developerId
              AND timestamp >= :since
            GROUP BY label
            """, nativeQuery = true)
    List<LabelCountProjection> aggregateLabelsForDeveloperSince(
            @Param("developerId") Long developerId,
            @Param("since") LocalDateTime since);

    @Query(value = """
            SELECT COUNT(*)
            FROM platformInformation
            WHERE developer_id = :developerId
              AND timestamp >= :since
            """, nativeQuery = true)
    long countTasksForDeveloperSince(
            @Param("developerId") Long developerId,
            @Param("since") LocalDateTime since);
}
