package com.SmartClassOllama.SmartClass.Scheduler.services;

import com.SmartClassOllama.SmartClass.Scheduler.Dto.ClassScheduleDTO;
import com.SmartClassOllama.SmartClass.Scheduler.Dto.PeriodDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Service
public class PdfService {
    public byte[] generatePdf(Map<String, List<PeriodDTO>> timetable) {

        try {
            Document document = new Document(PageSize.A4.rotate());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            // TITLE
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("WEEKLY TIMETABLE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // TABLE
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            table.addCell("Day");
            for (int i = 1; i <= 6; i++) {
                table.addCell("P" + i);
            }

            // ✅ FIXED DAY ORDER
            List<String> days = List.of("Monday","Tuesday","Wednesday","Thursday","Friday");

            for (String day : days) {

                table.addCell(day);

                List<PeriodDTO> periods = timetable.getOrDefault(day, List.of());

                for (int i = 1; i <= 6; i++) {

                    final int periodIndex = i;

                    PeriodDTO p = periods.stream()
                            .filter(x -> x.getPeriod() == periodIndex)
                            .findFirst()
                            .orElse(null);

                    StringBuilder cellText = new StringBuilder();

                    if (p != null && p.getClasses() != null) {
                        for (ClassScheduleDTO c : p.getClasses()) {
                            cellText.append(c.getSubject())
                                    .append(" (")
                                    .append(c.getTeacher())
                                    .append(" - ")
                                    .append(c.getRoom())
                                    .append(")\n");
                        }
                    }

                    table.addCell(cellText.length() > 0 ? cellText.toString() : "-");


                }
            }

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed: " + e.getMessage());
        }
    }

}


//    public byte[] generatePdf(Map<String, List<PeriodDTO>> timetable) {
//
//        try {
//            Document document = new Document();
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//            PdfWriter.getInstance(document, out);
//            document.open();
//
//            PdfPTable table = new PdfPTable(7);
//
//            table.addCell("Day");
//            for (int i = 1; i <= 6; i++) {
//                table.addCell("P" + i);
//            }
//
//            for (String day : timetable.keySet()) {
//                table.addCell(day);
//
//                List<PeriodDTO> periods = timetable.get(day);
//
//
//                for (int i = 1; i <= 6; i++) {
//
//                    final int periodIndex = i;   // ✅ FIX
//
//                    PeriodDTO p = periods.stream()
//                            .filter(x -> x.getPeriod() == periodIndex)
//                            .findFirst()
//                            .orElse(null);
//
//                    if (p != null) {
//                        table.addCell(p.getSubject() + "\n" + p.getTeacher());
//                    } else {
//                        table.addCell("-");
//                    }
//                }
//            }
//            document.add(table);
//            document.close();
//
//            return out.toByteArray();
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }

