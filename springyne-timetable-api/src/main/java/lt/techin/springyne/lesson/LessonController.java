package lt.techin.springyne.lesson;

import com.lowagie.text.DocumentException;
import lt.techin.springyne.pdfExporter.GroupLessonsPdfExporter;
import lt.techin.springyne.pdfExporter.RoomLessonPdfExporter;
import lt.techin.springyne.pdfExporter.TeacherLessonPdfExporter;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.room.RoomService;
import lt.techin.springyne.schedule.Schedule;
import lt.techin.springyne.schedule.ScheduleService;
import lt.techin.springyne.teacher.Teacher;
import lt.techin.springyne.teacher.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/lessons")
public class LessonController {

    @Autowired
    private final LessonService lessonService;
    @Autowired
    private final TeacherService teacherService;

    @Autowired
    private final RoomService roomService;

    @Autowired
    private final ScheduleService scheduleService;

    public LessonController(LessonService lessonService, TeacherService teacherService, RoomService roomService,
                            ScheduleService scheduleService) {
        this.lessonService = lessonService;
        this.teacherService = teacherService;
        this.roomService = roomService;
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<Lesson> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{lessonId}")
    public Optional<Lesson> getLessonById(@PathVariable Long lessonId) {
        return lessonService.getLessonById(lessonId);
    }

    @GetMapping("/schedule/{scheduleId}")
    public List<Lesson> getLessonsBySchedule(@PathVariable Long scheduleId) {
        return lessonService.getLessonsBySchedule(scheduleId);
    }

    @PostMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<Lesson>> addLesson(@Valid @RequestBody LessonBlock lessonBlock,
                                                  @PathVariable Long scheduleId,
                                                  @RequestParam Long subjectId,
                                                  @RequestParam(required = false) Long teacherId,
                                                  @RequestParam Long roomId) {
        return ResponseEntity.ok(lessonService.addLesson(lessonBlock, scheduleId,
                subjectId, teacherId, roomId));
    }

    @PatchMapping("/editSingleLesson/{lessonId}")
    public ResponseEntity<List<Lesson>> editLesson(@PathVariable Long lessonId,
                                                   @RequestParam Long subjectId,
                                                   @RequestParam(required = false) Long teacherId,
                                                   @RequestParam Long roomId) {
        return ResponseEntity.ok(lessonService.editSingleLesson(lessonId, subjectId, teacherId, roomId));
    }

    @PatchMapping("/editMultipleLessons/{scheduleId}")
    public ResponseEntity<List<Lesson>> editLessonList(@PathVariable Long scheduleId,
                                                       @RequestParam Long subjectId,
                                                       @RequestParam(required = false) Long teacherId,
                                                       @RequestParam Long roomId) {
        return ResponseEntity.ok(lessonService.editMultipleLessons(scheduleId, subjectId, teacherId, roomId));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<?> deleteSingleLesson(@PathVariable Long lessonId) {
        boolean isDeleted = lessonService.deleteLessonsByDateAndId(lessonId);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/schedule/{scheduleId}/export/pdf")
    public void exportToPdf(@PathVariable Long scheduleId, HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        Optional<Schedule> schedule = scheduleService.getScheduleById(scheduleId);
        String headerValue = "attachment; filename="+schedule.get().getGroup().getName()+"_pamoku_sarasas_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);

        List<Lesson> lessonsBySchedule = lessonService.getLessonsBySchedule(scheduleId);

        GroupLessonsPdfExporter exporter = new GroupLessonsPdfExporter(lessonsBySchedule, schedule);
        exporter.export(response);
    }

    @GetMapping("/teachers/export/pdf")
    public void exportTeacherLessonsToPDF(HttpServletResponse response,
                                          @RequestParam Long teacherId,
                                          @RequestParam String startDate,
                                          @RequestParam String endDate)
            throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        Optional<Teacher> teacher = teacherService.getTeacherById(teacherId);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+teacher.get().getName()+"_pamokos_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Lesson> listTeacherLessons = lessonService.listTeacherLessons(teacherId, startDate, endDate);


        TeacherLessonPdfExporter exporter = new TeacherLessonPdfExporter(listTeacherLessons, teacher);
        exporter.export(response);

    }
    @GetMapping("/rooms/export/pdf")
    public void exportRoomLessonsToPDF(HttpServletResponse response,
                                       @RequestParam String roomName,
                                       @RequestParam String startDate,
                                       @RequestParam String endDate)
            throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        Optional<Room> room = roomService.getAllRooms()
                .stream()
                .filter(r -> r.getName().equals(roomName))
                .findFirst();
        if (room.isEmpty()) {
            throw new IllegalArgumentException("Room with name " + roomName + " not found.");
        }
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+room.get().getName()+"_lessons_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Lesson> listRoomLessons = lessonService.listRoomLessons(room.get().getId(), startDate, endDate);

        RoomLessonPdfExporter exporter = new RoomLessonPdfExporter(listRoomLessons, room, startDate, endDate);
        exporter.export(response);
    }
}
