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
        return ResponseEntity.ok(service.listPatients());
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody @Valid PatientDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createPatient(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Patient> putById(@PathVariable UUID id, @RequestBody @Valid PatientDTO dto) {
        return ResponseEntity.ok(service.updatePatient(id, dto));
    }
}
