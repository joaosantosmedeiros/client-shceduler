package god.joaopedro.client_scheduler.commons.doctor.controller;

import god.joaopedro.client_scheduler.commons.doctor.model.Doctor;
import god.joaopedro.client_scheduler.commons.doctor.model.dto.DoctorDTO;
import god.joaopedro.client_scheduler.commons.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService service;

    @GetMapping
    public ResponseEntity<List<Doctor>> listDoctors() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("{id}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody @Valid DoctorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable UUID id, @RequestBody @Valid DoctorDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }
}
