package com.xmatrix.backend.web;

import com.xmatrix.backend.DTOs.*;
import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.SO;
import com.xmatrix.backend.service.SOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sos")
public class SOController {

    @Autowired
    private SOService soService;

    @GetMapping
    public List<SOResponse> getAllSOs() {
        return soService.getAllSOs();
    }

    @GetMapping("/{id}")
    public SOResponse getSOById(@PathVariable Long id) {
        return soService.getSOById(id);
    }

    @PostMapping
    public SOResponse createSO(@RequestBody SORequest so, @RequestParam Long xMatrixId) {
        return soService.createSO(so, xMatrixId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SOResponse> updateAO(@PathVariable Long id, @RequestBody SOUpdateDTO soUpdateDTO) {
        SOResponse updatedSO = soService.updateSO(id, soUpdateDTO);
        return new ResponseEntity<>(updatedSO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteSO(@PathVariable Long id) {
        soService.deleteSO(id);
    }

}
