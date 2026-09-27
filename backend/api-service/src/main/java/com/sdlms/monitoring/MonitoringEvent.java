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
@Table(name = "monitoring_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private Session session;

    @Column(name = "keystroke_count", nullable = false)
    @Builder.Default
    private Long keystrokeCount = 0L;

    @Column(name = "word_count", nullable = false)
    @Builder.Default
    private Long wordCount = 0L;

    @Column(name = "mouse_click_count", nullable = false)
    @Builder.Default
    private Long mouseClickCount = 0L;

    @Column(name = "mouse_distance_px", nullable = false)
    @Builder.Default
    private Long mouseDistancePx = 0L;

    @Column(name = "copy_paste_count", nullable = false)
    @Builder.Default
    private Long copyPasteCount = 0L;

    @Column(name = "idle_seconds", nullable = false)
    @Builder.Default
    private Long idleSeconds = 0L;

    @Column(name = "last_event_at", nullable = false)
    private OffsetDateTime lastEventAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.lastEventAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
