package github.com.LimaDevCod3r.models;

import github.com.LimaDevCod3r.models.enums.TimeRecordType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_time_records")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TimeRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp; // DataHora

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeRecordType type;

    @Column(name = "source_ip")
    private String sourceIp;

    @Column(columnDefinition = "TEXT")
    private String justification;

    @Column(name = "edited_by_user_id")
    private UUID editedByUserId;
}
