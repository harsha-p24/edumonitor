package com.sdlms.monitoring;

import com.sdlms.session.Session;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "engagement_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngagementStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private Session session;

    @Column(name = "is_engaged", nullable = false)
    @Builder.Default
    private Boolean isEngaged = false;

    @Column(name = "evaluated_at", nullable = false)
    private OffsetDateTime evaluatedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        this.evaluatedAt = OffsetDateTime.now();
    }
}
