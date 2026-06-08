package god.joaopedro.client_scheduler.commons.speciality.controller;

import god.joaopedro.client_scheduler.commons.speciality.model.Speciality;
import god.joaopedro.client_scheduler.commons.speciality.model.dto.SpecialityDTO;
import god.joaopedro.client_scheduler.commons.speciality.service.SpecialityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("speciality")
@RequiredArgsConstructor
public class SpecialityController {

    private final SpecialityService service;

    @GetMapping
    public ResponseEntity<List<Speciality>> listSpecialities() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Speciality> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Speciality> createSpeciality(@RequestBody @Valid SpecialityDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSpeciality(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Speciality> putById(@PathVariable UUID id, @RequestBody @Valid SpecialityDTO dto) {
        return ResponseEntity.ok(service.updateSpeciality(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        service.deleteSpeciality(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
