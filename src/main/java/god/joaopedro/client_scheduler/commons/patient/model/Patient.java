package god.joaopedro.client_scheduler.commons.patient.model;

import god.joaopedro.client_scheduler.commons.patient.model.dto.PatientDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Patient")
@Getter
@Setter
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(length = 11)
    private String phone;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Patient(PatientDTO dto) {
        this.name = dto.name();
        this.cpf = dto.cpf();
        this.phone = dto.phone();
        this.birthDate = dto.birthDate();
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
