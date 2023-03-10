package lt.techin.springyne.lesson;

import lt.techin.springyne.group.Group;
import lt.techin.springyne.group.GroupDto;
import lt.techin.springyne.program.Program;
import lt.techin.springyne.program.ProgramDto;
import lt.techin.springyne.program.ProgramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static lt.techin.springyne.group.GroupMapper.toGroup;
import static lt.techin.springyne.lesson.LessonMapper.toLesson;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/v1/lessons")
public class LessonController {

    @Autowired
    LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
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
                                                  @RequestParam Long teacherId,
                                                  @RequestParam Long roomId) {
        return ResponseEntity.ok(lessonService.addLesson(lessonBlock, scheduleId,
                subjectId, teacherId, roomId));
    }


    @PatchMapping("/editSingleLesson/{lessonId}") // koks???
    public ResponseEntity<Lesson> editLesson(@PathVariable Long lessonId,
                                             @RequestParam Long subjectId,
                                             @RequestParam Long teacherId,
                                             @RequestParam Long roomId) {
        return ResponseEntity.ok(lessonService.editSingleLesson(lessonId, subjectId, teacherId, roomId));
    }

    @PatchMapping("/editMultipleLessons/{lessonId}") // koks???
    public ResponseEntity<List<Lesson>> editLessonList(@PathVariable Long lessonId,
                                                       @RequestParam Long scheduleId,
                                                       @RequestParam Long subjectId,
                                                       @RequestParam Long teacherId,
                                                       @RequestParam Long roomId) {
        return ResponseEntity.ok(lessonService.editMultipleLessons(lessonId, scheduleId, subjectId, teacherId, roomId));
    }
}