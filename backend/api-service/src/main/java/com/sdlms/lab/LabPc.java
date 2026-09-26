package com.sdlms.lab;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lab_pcs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabPc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @Column(name = "pc_label", nullable = false, length = 30)
    private String pcLabel;

    @Column(length = 120)
    private String hostname;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
