package god.joaopedro.client_scheduler.commons.speciality.controller;

import god.joaopedro.client_scheduler.commons.speciality.model.Speciality;
import god.joaopedro.client_scheduler.commons.speciality.model.dto.SpecialityDTO;
import god.joaopedro.client_scheduler.commons.speciality.service.SpecialityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("speciality")
@RequiredArgsConstructor
public class SpecialityController {

    private final SpecialityService service;

    @PostMapping
    public Speciality createSpeciality(@RequestBody @Valid SpecialityDTO dto){
        return service.createSpeciality(dto);
    }
}
