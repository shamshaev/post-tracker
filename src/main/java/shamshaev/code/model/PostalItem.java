package shamshaev.code.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "postal_items")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class PostalItem implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{14}$")
    private String postalId;

    @Enumerated
    private PostalItemType type;

    @Pattern(regexp = "^[0-9]{6}$")
    private String recipientPostCode;

    @NotBlank
    private String recipientAddress;

    @NotBlank
    private String recipientName;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedDate
    private LocalDateTime createdAt;
}
