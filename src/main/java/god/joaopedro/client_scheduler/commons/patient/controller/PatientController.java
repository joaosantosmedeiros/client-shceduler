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

    @GetMapping("{email}")
    public ResponseEntity<Patient> getByEmail(@PathVariable String email) {
        Patient patient = service.getByCpf(email);
        if(patient == null) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        return ResponseEntity.ok(patient);
    }
}
