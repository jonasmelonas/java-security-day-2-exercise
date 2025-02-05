package com.booleanuk.api.controllers;

import com.booleanuk.api.models.CD;
import com.booleanuk.api.payload.response.CDListResponse;
import com.booleanuk.api.payload.response.CDResponse;
import com.booleanuk.api.payload.response.ErrorResponse;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repositories.CDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cds")
public class CDController {
    @Autowired
    private CDRepository cdRepository;

    @GetMapping
    public ResponseEntity<CDListResponse> getAllCDs() {
        CDListResponse cdListResponse = new CDListResponse();
        cdListResponse.set(this.cdRepository.findAll());
        return ResponseEntity.ok(cdListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createCD(@RequestBody CD cd) {
        CDResponse cdResponse = new CDResponse();
        try {
            cdResponse.set(this.cdRepository.save(cd));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cdResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getCDById(@PathVariable int id) {
        CD cd = this.cdRepository.findById(id).orElse(null);
        if (cd == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        CDResponse cdResponse = new CDResponse();
        cdResponse.set(cd);
        return ResponseEntity.ok(cdResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateCD(@PathVariable int id, @RequestBody CD cd) {
        CD cdToUpdate = this.cdRepository.findById(id).orElse(null);
        if (cdToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        cdToUpdate.setTitle(cd.getTitle());
        cdToUpdate.setArtist(cd.getArtist());
        cdToUpdate.setReleaseYear(cd.getReleaseYear());

        try {
            cdToUpdate = this.cdRepository.save(cdToUpdate);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        CDResponse cdResponse = new CDResponse();
        cdResponse.set(cdToUpdate);
        return new ResponseEntity<>(cdResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteCD(@PathVariable int id) {
        CD cdToDelete = this.cdRepository.findById(id).orElse(null);
        if (cdToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.cdRepository.delete(cdToDelete);
        CDResponse cdResponse = new CDResponse();
        cdResponse.set(cdToDelete);
        return ResponseEntity.ok(cdResponse);
    }
}