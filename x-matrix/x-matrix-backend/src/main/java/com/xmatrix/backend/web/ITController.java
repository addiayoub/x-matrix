package com.xmatrix.backend.web;

import com.xmatrix.backend.DTOs.*;
import com.xmatrix.backend.entity.IT;
import com.xmatrix.backend.service.ITService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/its")
public class ITController {

    @Autowired
    private ITService itService;

    @GetMapping
    public List<ITResponse> getAllITs() {
        return itService.getAllITs();
    }

    @GetMapping("/{id}")
    public ITResponse getITById(@PathVariable Long id) {
        return itService.getITById(id);
    }

    @PostMapping
    public ITResponse createIT(@RequestBody ITRequest it, @RequestParam Long ipId) {
        return itService.createIT(it,ipId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ITResponse> updateAO(@PathVariable Long id, @RequestBody ITUpdateDTO itUpdateDTO) {
        ITResponse updatedIT = itService.updateIT(id, itUpdateDTO);
        return new ResponseEntity<>(updatedIT, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteIT(@PathVariable Long id) {
        itService.deleteIT(id);
    }
}
