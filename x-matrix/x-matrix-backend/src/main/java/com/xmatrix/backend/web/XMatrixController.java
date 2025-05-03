package com.xmatrix.backend.web;

import com.xmatrix.backend.DTOs.*;
import com.xmatrix.backend.entity.User;
import com.xmatrix.backend.service.ExcelService;
import com.xmatrix.backend.service.XMatrixService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/xmatrix/")
public class XMatrixController {
    @Autowired
    private XMatrixService xMatrixService;
    @Autowired
    private ExcelService excelService;

    @GetMapping("/all")
    public List<XMatrixResponse> getAllXMatrices() {
        return xMatrixService.getAllXMatrices();
    }

    @GetMapping("/{id}")
    public XMatrixResponse getXMatrixById(@PathVariable Long id) {
        return xMatrixService.getXMatrixById(id);
    }

    @GetMapping("")
    public XMatrixResponse getXMatrixById() {
        return xMatrixService.getXMatrixByUserConnected();
    }

    @PostMapping
    public ResponseEntity<?> createXMatrix(@RequestBody XMatrixRequest xMatrix) {
        try {
            return ResponseEntity.ok(xMatrixService.createXMatrix(xMatrix));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Xmatrix already exists");
        }
    }

    @PutMapping("/{id}")
    public XMatrixResponse updateXMatrix(@PathVariable Long id, @RequestBody XMatrixRequestDTO xMatrixRequestDTO) {
        return xMatrixService.updateXMatrix(id, xMatrixRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteXMatrix(@PathVariable Long id) {
        xMatrixService.deleteXMatrix(id);
    }

    @GetMapping("/{id}/sos")
    public List<SOResponse> getAllSOsByXMatrix(@PathVariable Long id) {
        return xMatrixService.getAllSOsByXMatrix(id);
    }
    @GetMapping("/{id}/aos")
    public List<AOResponse> getAllAOsByXMatrix(@PathVariable Long id) {
        return xMatrixService.getAllAOsByXMatrix(id);
    }
    @GetMapping("/{id}/ips")
    public List<IPResponse> getAllIPsByXMatrix(@PathVariable Long id) {
        return xMatrixService.getAllIPsByXMatrix(id);
    }
    @GetMapping("/{id}/its")
    public List<ITResponse> getAllITsByXMatrix(@PathVariable Long id) {
        return xMatrixService.getAllITsByXMatrix(id);
    }
    @GetMapping("/{id}/resources")
    public List<ResourceResponseDTO> getAllResourcessByXMatrix(@PathVariable Long id) {
        return xMatrixService.getAllResourcessByXMatrix(id);
    }

    @PostMapping("{id}/upload")
    public ResponseEntity<?> uploadExcelFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return ResponseEntity.badRequest().body(null);
        }

        excelService.upload(id,file);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("{id}/export")
    public ResponseEntity<?> exportXMatrix(@PathVariable Long id) throws Exception {
        Workbook workbook = excelService.export(id);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=xmatrix_export_" + id + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

    @GetMapping("/matrices-under-hierarchy")
    public List<XMatrixResponse> getMatricesUnderHierarchy(){
        return xMatrixService.getMatricesUnderHierarchy();

    }




}
