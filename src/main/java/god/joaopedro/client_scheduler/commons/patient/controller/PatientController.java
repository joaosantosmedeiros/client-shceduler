package god.joaopedro.client_scheduler.commons.patient.controller;

import god.joaopedro.client_scheduler.commons.patient.model.Patient;
import god.joaopedro.client_scheduler.commons.patient.model.dto.PatientDTO;
import god.joaopedro.client_scheduler.commons.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("patients")
public class PatientController {

    private final PatientService service;

    @GetMapping
    public ResponseEntity<List<Patient>> listPatients() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody @Valid PatientDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping("{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable UUID id, @RequestBody @Valid PatientDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePatient (@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Patient> activatePatient(@PathVariable UUID id) {
        service.activate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
