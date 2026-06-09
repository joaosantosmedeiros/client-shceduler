package god.joaopedro.client_scheduler.commons.doctor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Doctor")
@Getter
@Setter
@NoArgsConstructor
public class Doctor {

    public Doctor(UUID id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 15)
    private String crm;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(length = 11)
    private String phone;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}
