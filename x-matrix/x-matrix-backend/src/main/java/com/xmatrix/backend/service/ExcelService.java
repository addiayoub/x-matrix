package com.xmatrix.backend.service;

import com.xmatrix.backend.DTOs.*;
import com.xmatrix.backend.entity.*;
import com.xmatrix.backend.repository.ResourceRepository;
import com.xmatrix.backend.repository.XMatrixRepository;
import com.xmatrix.backend.schedules.Scheduler;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private final XMatrixService xMatrixService;
    private final SOService soService;
    private final AOService aoService;
    private final IPService ipService;
    private final ResourceService resourceService;
    private final ITService itService;
    private final XMatrixRepository xMatrixRepository;
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ResourceRepository resourceRepository;
    private final Scheduler scheduler;
    private final UserService userService;


    public void upload(Long id, MultipartFile file) throws Exception {
        Optional<XMatrix> xMatrix = xMatrixRepository.findById(id);

        if(xMatrix.isEmpty()) {
            throw new Exception("XMatrix not founded!");
        }

        // First delete all existing hierarchy
        deleteExistingHierarchy(xMatrix.get());

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            // Persist Resources
            List<ResourceExcelDTO> resources = readResources(workbook);
            Map<String, Long> resourceIds = new HashMap<>();
            for(ResourceExcelDTO resource : resources) {
                try{
                    Resource r = new Resource();
                    r.setDepartment(resource.getDepartement());
                    User user = userService.getConnectedUser();
                    r.setCompany(user.getCompany());
                    Resource resourceResponseDTO = resourceRepository.save(r);
                    resourceIds.put(resource.getId(),resourceResponseDTO.getId());
                }catch (Exception e){
                    System.err.println(e.getMessage());
                }
            }

            List<ExcelDTO> sos = readStrategicObjectives(workbook);
            for(ExcelDTO so : sos){
                System.out.println("SO: "+ so.getLabel()+ " | Start: "+so.getStart()+" | End: "+so.getEnd());
                SORequest soRequest = new SORequest();
                soRequest.setLabel(so.getLabel());
                soRequest.setStart(so.getStart());
                soRequest.setEnd(so.getEnd());

                System.out.println("SORequest: "+soRequest.getLabel()+ " | Start: "+so.getStart()+" | End: "+so.getEnd());
                SOResponse soResponse = soService.createSO(soRequest,xMatrix.get().getId());
                List<AOExcelDTO> aos = readAOs(workbook);
                for(AOExcelDTO ao : aos){
                    if(ao.getSoId().equals(so.getId())){
                        System.out.println("\t - AO: "+ ao.getLabel());
                        AORequest aoRequest = new AORequest();
                        aoRequest.setLabel(ao.getLabel());
                        aoRequest.setStart(ao.getStart());
                        aoRequest.setEnd(ao.getEnd());
                        AOResponse aoResponse = aoService.createAO(aoRequest,soResponse.getId());
                        System.out.println("AORequest: "+aoRequest.getLabel()+ " | Start: "+ao.getStart()+" | End: "+ao.getEnd());

                        List<IPExcelDTO> ips = readIPs(workbook);
                        for (IPExcelDTO ip : ips) {
                            if(ip.getAoId().equals(ao.getId())){
                                System.out.println("\t\t - IP: "+ ip.getLabel()+ " | Start: "+ip.getStart()+" | End: "+ip.getEnd());
                                IPRequest ipRequest = new IPRequest();
                                ipRequest.setLabel(ip.getLabel());
                                ipRequest.setStart(ip.getStart());
                                ipRequest.setEnd(ip.getEnd());
                                System.out.println("IPRequest: "+ ipRequest.getLabel()+ " | Start: "+ip.getStart()+" | End: "+ip.getEnd());
                                IPResponse ipResponse = ipService.createIP(ipRequest,aoResponse.getId());


                                List<ITExcelDTO> its = readITs(workbook);
                                for(ITExcelDTO it: its){
                                    if(it.getIpId().equals(ip.getId())){
                                        System.out.println("\t\t\t - IT: "+ it.getLabel()+ " | Start: "+so.getStart()+" | End: "+so.getEnd());
                                        ITRequest itRequest = new ITRequest();
                                        itRequest.setLabel(it.getLabel());
                                        itRequest.setStart(it.getStart());
                                        itRequest.setEnd(it.getEnd());
                                        System.out.println("Avancement: "+it.getAdvancement());
                                        itRequest.setAdvancement(it.getAdvancement());
                                        itRequest.setResourceId(resourceIds.get(it.getResourceId()));
                                        ITResponse itResponse = itService.createIT(itRequest,ipResponse.getId());
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        scheduler.run();
    }

    private void deleteExistingHierarchy(XMatrix xMatrix) {
        // Delete all ITs
        itService.deleteAllByXMatrixId(xMatrix.getId());

        // Delete all IPs
        ipService.deleteAllByXMatrixId(xMatrix.getId());

        // Delete all AOs
        aoService.deleteAllByXMatrixId(xMatrix.getId());

        // Delete all SOs
        soService.deleteAllByXMatrixId(xMatrix.getId());

        // Optionally delete resources if needed
        // resourceService.deleteAllByXMatrixId(xMatrix.getId());
    }

    private Date parseDateTime(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                return sdf.parse(cell.getStringCellValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<ExcelDTO> readStrategicObjectives(Workbook workbook) {
        Sheet sheet = workbook.getSheet("SO");
        List<ExcelDTO> strategicObjectives = new ArrayList<>();

        if (sheet == null) {
            return strategicObjectives;
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            ExcelDTO so = new ExcelDTO();
            so.setId(getStringValue(row.getCell(0)));
            so.setLabel(getStringValue(row.getCell(1)));
            Date start = parseDateTime(row.getCell(3));
            so.setStart(start);
            Date end = parseDateTime(row.getCell(4));
            so.setEnd(end);
            if(so.getLabel()!= null && !so.getLabel().isEmpty()){
                strategicObjectives.add(so);
            }

        }

        return strategicObjectives;
    }

    private List<AOExcelDTO> readAOs(Workbook workbook) {
        Sheet sheet = workbook.getSheet("AO");
        List<AOExcelDTO> aos = new ArrayList<>();

        if (sheet == null) {
            return aos;
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            AOExcelDTO ao = new AOExcelDTO();
            ao.setId(getStringValue(row.getCell(0)));
            ao.setLabel(getStringValue(row.getCell(1)));
            ao.setSoId(getStringValue(row.getCell(2)));
            Date start = parseDateTime(row.getCell(4));
            ao.setStart(start);
            Date end = parseDateTime(row.getCell(5));
            ao.setEnd(end);
            if(ao.getLabel()!= null && !ao.getLabel().isEmpty()){
                aos.add(ao);
            }

        }

        return aos;
    }

    private List<IPExcelDTO> readIPs(Workbook workbook) {
        Sheet sheet = workbook.getSheet("II");
        List<IPExcelDTO> ips = new ArrayList<>();

        if (sheet == null) {
            return ips;
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            IPExcelDTO ip = new IPExcelDTO();
            ip.setId(getStringValue(row.getCell(0)));
            ip.setLabel(getStringValue(row.getCell(1)));
            ip.setAoId(getStringValue(row.getCell(2)));
            Date start = parseDateTime(row.getCell(5));
            ip.setStart(start);
            Date end = parseDateTime(row.getCell(6));
            ip.setEnd(end);

            if(ip.getLabel()!= null && !ip.getLabel().isEmpty()){
                ips.add(ip);
            }

        }
        return ips;
    }
    private List<ITExcelDTO> readITs(Workbook workbook) {
        Sheet sheet = workbook.getSheet("IT");
        List<ITExcelDTO> its = new ArrayList<>();

        if (sheet == null) {
            return its;
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            ITExcelDTO it = new ITExcelDTO();
            it.setId(getStringValue(row.getCell(0)));
            it.setLabel(getStringValue(row.getCell(1)));
            it.setIpId(getStringValue(row.getCell(2)));
            Date start = parseDateTime(row.getCell(4));
            it.setStart(start);
            Date end = parseDateTime(row.getCell(5));
            it.setEnd(end);
            Double avancement = getPercentageValue(row.getCell(3));
            it.setAdvancement(avancement);
            if(it.getLabel()!= null && !it.getLabel().isEmpty()){
                its.add(it);
            }

            it.setResourceId(getStringValue(row.getCell(10)));

        }

        return its;
    }

    private List<ResourceExcelDTO> readResources(Workbook workbook) {
        Sheet sheet = workbook.getSheet("Resources");
        List<ResourceExcelDTO> resources = new ArrayList<>();

        if (sheet == null) {
            return resources;
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            ResourceExcelDTO resource = new ResourceExcelDTO();
            resource.setId(getStringValue(row.getCell(0)));
            resource.setDepartement(getStringValue(row.getCell(1)));

            if(resource.getDepartement()!= null && !resource.getDepartement().isEmpty()){
                resources.add(resource);
            }

        }

        return resources;
    }

    private String getStringValue(Cell cell) {
        if (cell == null) return "";

        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
    private Double getPercentageValue(Cell cell){

        Double value = 0.0;

        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                // If it's numeric, assume it's a percentage stored as a decimal (e.g., 0.5 for 50%)
                value = cell.getNumericCellValue() * 100;
            } else if (cell.getCellType() == CellType.STRING) {
                // If it's a string, clean up and parse it
                value = Double.parseDouble(cell.getStringCellValue().replace("%", "").trim());
            }
        }

        return value;
    }
    private Double getNumericValue(Cell cell) {
        if (cell == null) return 0.0;

        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }

        switch (cellType) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            default:
                return 0.0;
        }
    }

    private void createHeaders(Sheet sheet, String sheetName, String[] headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    public Workbook export(Long xMatrixId) throws Exception {
        Optional<XMatrix> xMatrix = xMatrixRepository.findById(xMatrixId);

        if (xMatrix.isEmpty()) {
            throw new Exception("XMatrix not found!");
        }

        Workbook workbook = new XSSFWorkbook();

        // Create sheets
        Sheet soSheet = workbook.createSheet("SO");
        Sheet aoSheet = workbook.createSheet("AO");
        Sheet ipSheet = workbook.createSheet("IP");
        Sheet itSheet = workbook.createSheet("IT");
        Sheet resourceSheet = workbook.createSheet("Resources");

        // Create headers for each sheet
        createHeaders(soSheet, "SO", new String[]{"ID", "Label", "Advancement", "Start Date", "End Date", "Period Length", "Time Spent", "Progress Time", "Trend"});
        createHeaders(aoSheet, "AO", new String[]{"ID", "Label", "SO ID", "Advancement", "Start Date", "End Date", "Period Length", "Time Spent", "Progress Time", "Trend"});
        createHeaders(ipSheet, "IP", new String[]{"ID", "Label", "AO ID", "Advancement", "Start Date", "End Date", "Period Length", "Time Spent", "Progress Time", "Trend"});
        createHeaders(itSheet, "IT", new String[]{"ID", "Label", "IP ID", "Advancement", "Start Date", "End Date", "Period Length", "Time Spent", "Progress Time", "Trend",  "Resource ID"});
        createHeaders(resourceSheet, "Resources", new String[]{"ID", "Department", "Actual Progress", "Timely Progress", "Trend"});

        // Get all data
        List<SOResponse> sos = xMatrixService.getAllSOsByXMatrix(xMatrixId);
        List<AOResponse> aos = xMatrixService.getAllAOsByXMatrix(xMatrixId);
        List<IPResponse> ips = xMatrixService.getAllIPsByXMatrix(xMatrixId);
        List<ITResponse> its = xMatrixService.getAllITsByXMatrix(xMatrixId);
        List<ResourceResponseDTO> resources = xMatrixService.getAllResourcessByXMatrix(xMatrixId);

        // Fill SO sheet
        fillSOSheet(soSheet, sos);

        // Fill AO sheet
        fillAOSheet(aoSheet, aos);

        // Fill IP sheet
        fillIPSheet(ipSheet, ips);

        // Fill IT sheet
        fillITSheet(itSheet, its);

        // Fill Resources sheet
        fillResourcesSheet(resourceSheet,resources);

        return workbook;
    }
    private CellStyle createDateCellStyle(Workbook workbook) {
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
        return dateStyle;
    }

    private void fillSOSheet(Sheet sheet, List<SOResponse> sos) {
        CellStyle dateStyle = createDateCellStyle(sheet.getWorkbook());

        // Format header date columns
        sheet.getRow(0).getCell(3).setCellStyle(dateStyle);
        sheet.getRow(0).getCell(4).setCellStyle(dateStyle);

        int rowNum = 1;
        for (SOResponse so : sos) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(so.getId());
            row.createCell(1).setCellValue(so.getLabel());
            row.createCell(2).setCellValue(so.getAdvancement());

            // Start Date
            if (so.getStart() != null) {
                Cell cell = row.createCell(3);
                cell.setCellValue(so.getStart());
                cell.setCellStyle(dateStyle);
            }

            // End Date
            if (so.getEnd() != null) {
                Cell cell = row.createCell(4);
                cell.setCellValue(so.getEnd());
                cell.setCellStyle(dateStyle);
            }

            row.createCell(5).setCellValue(so.getPeriodLength());
            row.createCell(6).setCellValue(so.getTimeSpent());
            row.createCell(7).setCellValue(so.getProgressTime());
            row.createCell(8).setCellValue(so.getTrend().toString());
        }

        // Auto-size all columns
        autoSizeColumns(sheet, 9);
    }

    private void autoSizeColumns(Sheet sheet, int numColumns) {
        for (int i = 0; i < numColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void fillAOSheet(Sheet sheet, List<AOResponse> aos) {

        CellStyle dateStyle = createDateCellStyle(sheet.getWorkbook());

        // Format header date columns
        sheet.getRow(0).getCell(4).setCellStyle(dateStyle);
        sheet.getRow(0).getCell(5).setCellStyle(dateStyle);

        int rowNum = 1;
        for (AOResponse ao : aos) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(ao.getId());
            row.createCell(1).setCellValue(ao.getLabel());
            row.createCell(2).setCellValue(ao.getSoId());
            row.createCell(3).setCellValue(ao.getAdvancement());
            // Add description if available


            // Start Date
            if (ao.getStart() != null) {
                Cell cell = row.createCell(4);
                cell.setCellValue(ao.getStart());
                cell.setCellStyle(dateStyle);
            }

            // End Date
            if (ao.getEnd() != null) {
                Cell cell = row.createCell(5);
                cell.setCellValue(ao.getEnd());
                cell.setCellStyle(dateStyle);
            }


            row.createCell(6).setCellValue(ao.getPeriodLength());
            row.createCell(7).setCellValue(ao.getTimeSpent());
            row.createCell(8).setCellValue(ao.getProgressTime());
            row.createCell(9).setCellValue(ao.getTrend().toString());

        }

        // Auto-size all columns
        autoSizeColumns(sheet, 9);
    }

    private void fillIPSheet(Sheet sheet, List<IPResponse> ips) {

        CellStyle dateStyle = createDateCellStyle(sheet.getWorkbook());

        // Format header date columns
        sheet.getRow(0).getCell(4).setCellStyle(dateStyle);
        sheet.getRow(0).getCell(5).setCellStyle(dateStyle);
        int rowNum = 1;
        for (IPResponse ip : ips) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(ip.getId());
            row.createCell(1).setCellValue(ip.getLabel());
            row.createCell(2).setCellValue(ip.getAoId());
            row.createCell(3).setCellValue(ip.getAdvancement());
            // Add description if available


            // Start Date
            if (ip.getStart() != null) {
                Cell cell = row.createCell(4);
                cell.setCellValue(ip.getStart());
                cell.setCellStyle(dateStyle);
            }

            // End Date
            if (ip.getEnd() != null) {
                Cell cell = row.createCell(5);
                cell.setCellValue(ip.getEnd());
                cell.setCellStyle(dateStyle);
            }


            row.createCell(6).setCellValue(ip.getPeriodLength());
            row.createCell(7).setCellValue(ip.getTimeSpent());
            row.createCell(8).setCellValue(ip.getProgressTime());
            row.createCell(9).setCellValue(ip.getTrend().toString());

        }
        // Auto-size all columns
        autoSizeColumns(sheet, 9);
    }

    private void fillITSheet(Sheet sheet, List<ITResponse> its) {
        CellStyle dateStyle = createDateCellStyle(sheet.getWorkbook());

        // Format header date columns
        sheet.getRow(0).getCell(4).setCellStyle(dateStyle);
        sheet.getRow(0).getCell(5).setCellStyle(dateStyle);

        int rowNum = 1;
        for (ITResponse it : its) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(it.getId());
            row.createCell(1).setCellValue(it.getLabel());
            row.createCell(2).setCellValue(it.getIpId());
            row.createCell(3).setCellValue(it.getAdvancement());
            // Add description if available


            // Start Date
            if (it.getStart() != null) {
                Cell cell = row.createCell(4);
                cell.setCellValue(it.getStart());
                cell.setCellStyle(dateStyle);
            }

            // End Date
            if (it.getEnd() != null) {
                Cell cell = row.createCell(5);
                cell.setCellValue(it.getEnd());
                cell.setCellStyle(dateStyle);
            }


            row.createCell(6).setCellValue(it.getPeriodLength());
            row.createCell(7).setCellValue(it.getTimeSpent());
            row.createCell(8).setCellValue(it.getProgressTime());
            row.createCell(9).setCellValue(it.getTrend().toString());
            row.createCell(10).setCellValue(it.getResourceId());

        }
        // Auto-size all columns
        autoSizeColumns(sheet, 9);
    }

    private void fillResourcesSheet(Sheet sheet, List<ResourceResponseDTO> resources) {
        int rowNum = 1;
        for (ResourceResponseDTO resource : resources) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(resource.getId());
            row.createCell(1).setCellValue(resource.getDepartment());
            row.createCell(2).setCellValue(resource.getActualProgress());
            row.createCell(3).setCellValue(resource.getTimelyProgress());
            row.createCell(4).setCellValue(resource.getTrend().toString());


        }
    }


}