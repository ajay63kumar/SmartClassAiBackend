
package com.SmartClassOllama.SmartClass.Scheduler.controller;

import com.SmartClassOllama.SmartClass.Scheduler.Dto.PeriodDTO;
import com.SmartClassOllama.SmartClass.Scheduler.services.PdfService;
import com.SmartClassOllama.SmartClass.Scheduler.services.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PdfController {

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private PdfService pdfService;

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @RequestBody Map<String, List<PeriodDTO>> timetable) {

        byte[] pdf = pdfService.generatePdf(timetable);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=timetable.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}



