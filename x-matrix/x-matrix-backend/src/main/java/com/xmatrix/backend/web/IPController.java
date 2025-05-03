package com.xmatrix.backend.web;

import com.xmatrix.backend.DTOs.*;
import com.xmatrix.backend.entity.IP;
import com.xmatrix.backend.entity.IT;
import com.xmatrix.backend.service.IPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ips")
public class IPController {

    @Autowired
    private IPService ipService;

    @GetMapping
    public List<IPResponse> getAllIPs() {
        return ipService.getAllIPs();
    }

    @GetMapping("/{id}")
    public IPResponse getIPById(@PathVariable Long id) {
        return ipService.getIPById(id);
    }

    @PostMapping
    public IPResponse createIP(@RequestBody IPRequest ip, @RequestParam Long aoId) {
        return ipService.createIP(ip, aoId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IPResponse> updateAO(@PathVariable Long id, @RequestBody IPUpdateDTO ipUpdateDTO) {
        IPResponse updatedIP = ipService.updateIP(id, ipUpdateDTO);
        return new ResponseEntity<>(updatedIP, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteIP(@PathVariable Long id) {
        ipService.deleteIP(id);
    }


}
