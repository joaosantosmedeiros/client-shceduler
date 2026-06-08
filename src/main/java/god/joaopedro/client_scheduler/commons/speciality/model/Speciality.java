package god.joaopedro.client_scheduler.commons.speciality.model;

import god.joaopedro.client_scheduler.commons.speciality.model.dto.SpecialityDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Table(name = "Speciality")
@Getter
@Setter
public class Speciality {

    public Speciality(SpecialityDTO dto) {
        this.name = dto.name();
        this.createdAt = LocalDateTime.now();
        this.isActive = Boolean.TRUE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
