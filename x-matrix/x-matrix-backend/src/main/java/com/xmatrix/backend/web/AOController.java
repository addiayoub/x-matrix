package com.xmatrix.backend.web;

import com.xmatrix.backend.DTOs.AORequest;
import com.xmatrix.backend.DTOs.AOResponse;
import com.xmatrix.backend.DTOs.AOUpdateDTO;
import com.xmatrix.backend.entity.AO;
import com.xmatrix.backend.entity.IP;
import com.xmatrix.backend.service.AOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aos")
public class AOController {

    @Autowired
    private AOService aoService;

    @GetMapping
    public List<AOResponse> getAllAOs() {
        return aoService.getAllAOs();
    }

    @GetMapping("/{id}")
    public AOResponse getAOById(@PathVariable Long id) {
        return aoService.getAOById(id);
    }

    @PostMapping
    public AOResponse createAO(@RequestBody AORequest ao, @RequestParam Long soId) {
        return aoService.createAO(ao,soId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AOResponse> updateAO(@PathVariable Long id, @RequestBody AOUpdateDTO aoUpdateDTO) {
        AOResponse updatedAO = aoService.updateAO(id, aoUpdateDTO);
        return new ResponseEntity<>(updatedAO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteAO(@PathVariable Long id) {
        aoService.deleteAO(id);
    }

}
