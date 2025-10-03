package com.vaggelis.SpringSchool.controller;

import com.vaggelis.SpringSchool.dto.SpecialityInsertDTO;
import com.vaggelis.SpringSchool.dto.SpecialityReadDTO;
import com.vaggelis.SpringSchool.exception.speciality.SpecialityAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.speciality.SpecialityNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Speciality;
import com.vaggelis.SpringSchool.service.speciality.ISpecialityService;
import com.vaggelis.SpringSchool.validator.SpecialityValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/speciality")
public class SpecialityController {

    private final ISpecialityService specialityService;
    private final SpecialityValidator specialityValidator;

    @PostMapping("/add")
    public ResponseEntity<SpecialityReadDTO> addSpeciality(@Valid @RequestBody SpecialityInsertDTO dto, BindingResult bindingResult){
        specialityValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Speciality speciality = specialityService.addSpeciality(dto);
            SpecialityReadDTO readDTO = Mapper.mapReadDtoSpeciality(speciality);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readDTO.getId())
                    .toUri();
            return ResponseEntity.created(location).body(readDTO);
        } catch (SpecialityAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/speciality/{id}")
    public ResponseEntity<SpecialityReadDTO> getSpeciality(@PathVariable Long id){
        try {
            Speciality speciality = specialityService.findSpecialityById(id);
            SpecialityReadDTO readDTO = Mapper.mapReadDtoSpeciality(speciality);
            return new ResponseEntity<>(readDTO, HttpStatus.OK);
        } catch (SpecialityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/speciality/all")
    public ResponseEntity<List<SpecialityReadDTO>> getSllSpecialities(){
        try {
            List<Speciality> specialities = specialityService.findAllSpecialities();

            List<SpecialityReadDTO> readDTOS = new ArrayList<>();

            for (Speciality speciality : specialities){
                readDTOS.add(Mapper.mapReadDtoSpeciality(speciality));
            }
            return new ResponseEntity<>(readDTOS, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/speciality/delete/{id}")
    public ResponseEntity<Speciality> deleteSpeciality(@PathVariable Long id){
        try {
            Speciality speciality = specialityService.deleteSpeciality(id);
            return ResponseEntity.ok(speciality);
        } catch (SpecialityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
