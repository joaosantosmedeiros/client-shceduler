package god.joaopedro.client_scheduler.commons.speciality.controller;

import god.joaopedro.client_scheduler.commons.speciality.model.Speciality;
import god.joaopedro.client_scheduler.commons.speciality.model.dto.SpecialityDTO;
import god.joaopedro.client_scheduler.commons.speciality.service.SpecialityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("speciality")
@RequiredArgsConstructor
public class SpecialityController {

    private final SpecialityService service;

    @GetMapping
    public List<Speciality> listSpecialities() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public Speciality getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public Speciality createSpeciality(@RequestBody @Valid SpecialityDTO dto){
        return service.createSpeciality(dto);
    }
}
