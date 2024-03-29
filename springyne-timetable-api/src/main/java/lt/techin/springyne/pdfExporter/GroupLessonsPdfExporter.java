package lt.techin.springyne.pdfExporter;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.*;
import lt.techin.springyne.lesson.Lesson;
import lt.techin.springyne.schedule.Schedule;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class GroupLessonsPdfExporter {

    private final List<Lesson> lessonsBySchedule;

    private final Optional<Schedule> schedule;

    private BaseFont baseFont = BaseFont.createFont("fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

    public GroupLessonsPdfExporter(List<Lesson> lessonsBySchedule, Optional<Schedule> schedule) throws IOException {
        this.lessonsBySchedule = lessonsBySchedule;
        this.schedule = schedule;}


    void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(5);

        Font font = new Font(baseFont, 12);

        cell.setPhrase(new Phrase("Data", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Laikas", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Dalykas", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Mokytojas", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Kabinetas", font));
        table.addCell(cell);

    }

    void writeTableData(PdfPTable table) {
        Font font = new Font(baseFont, 12);
        LocalDate previousDate = null;

        for (Lesson lesson : lessonsBySchedule) {
            LocalDate currentDate = lesson.getLessonDate();

            if (previousDate == null || !currentDate.isEqual(previousDate)) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(currentDate), font)));
                previousDate = currentDate;
            } else {
                table.addCell(new PdfPCell(new Phrase("", font))); // add empty cell to maintain table structure
            }

            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getLessonTime()), font)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getSubject().getName()), font)));
            if (lesson.getTeacher() == null) {
                table.addCell(new PdfPCell(new Phrase("", font)));
            } else {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getTeacher().getName()), font)));
            }
            table.addCell(new PdfPCell(new Phrase(String.valueOf(lesson.getRoom().getName()), font)));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = new Font(baseFont, 18);

        Paragraph paragraph = new Paragraph(schedule.get().getName(), font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2.5f, 1.5f, 4.0f, 3.0f, 2.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();


    }
}