package god.joaopedro.client_scheduler.commons.doctor.model;

import god.joaopedro.client_scheduler.commons.doctor.model.dto.DoctorDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
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

    public Doctor(DoctorDTO dto) {
        if(dto == null) return;

        this.name = dto.name();
        this.crm = dto.crm();
        this.cpf = dto.cpf();
        this.phone = dto.phone();
        this.birthDate = dto.birthDate();
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
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
    private Boolean isActive;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}
