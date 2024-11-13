package shamshaev.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "statuses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Status implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @Enumerated
    private StatusType type;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PostalItem postalItem;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PostOffice postOffice;

    @CreatedDate
    private LocalDateTime createdAt;
}
