package lt.techin.springyne.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static lt.techin.springyne.subject.SubjectMapper.toSubject;
import static lt.techin.springyne.subject.SubjectMapper.toSubjectDto;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/subjects")

public class SubjectController {

    @Autowired
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/search")
    public Page<Subject> searchByNamePaged(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String moduleName,
                                           @RequestParam int page,
                                           @RequestParam int pageSize) {
        return subjectService.searchByNamePaged(name, moduleName, page, pageSize);
    }


    @GetMapping("/{subjectId}")
    public Optional<Subject> getSubject(@PathVariable Long subjectId) {
        return subjectService.getById(subjectId);
    }

    @PostMapping(value = "/createSubject")
    public ResponseEntity<SubjectDto> createSubject(@RequestBody SubjectDto subjectDto,
                                                    @RequestParam Long moduleId,
                                                    @RequestParam(required = false) Long roomId) {

        return ok(toSubjectDto(subjectService.createSubject(moduleId, roomId, toSubject(subjectDto))));
    }


    @PatchMapping("/edit/{subjectId}")
    public ResponseEntity<Subject> editSubject(@PathVariable Long subjectId,
                                               @RequestBody SubjectDto subjectDto,
                                               @RequestParam(required = false) Long moduleId,
                                               @RequestParam(required = false) Long roomId) {

        return ok(subjectService.edit(subjectId, toSubject(subjectDto), moduleId, roomId));
    }


    @PatchMapping("/delete/{subjectId}")
    public ResponseEntity<Subject> deleteSubject(@PathVariable Long subjectId) {

        return ok(subjectService.delete(subjectId));

    }

    @PatchMapping("/restore/{subjectId}")
    public ResponseEntity<Subject> restoreSubject(@PathVariable Long subjectId) {

        return ok(subjectService.restore(subjectId));

    }

    @PatchMapping("/{subjectId}/deleteRoom/{roomId}")
    public void deleteRoomFromSubject(@PathVariable Long subjectId,
                                      @PathVariable Long roomId) {
        subjectService.deleteRoomFromSubject(subjectId, roomId);
    }

    @PatchMapping("/{subjectId}/addModule/{moduleId}")
    public ResponseEntity<Subject> addModuleToSubject(@PathVariable Long subjectId, @PathVariable Long moduleId) {
        return ResponseEntity.ok(subjectService.addModuleToSubject(subjectId, moduleId));
    }
}