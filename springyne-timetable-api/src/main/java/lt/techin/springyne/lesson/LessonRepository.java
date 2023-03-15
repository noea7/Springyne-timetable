package lt.techin.springyne.lesson;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson,Long> {

    List<Lesson> findByScheduleId(Long scheduleId);

    List<Lesson> findByLessonDateBetweenAndLessonTimeBetween(LocalDate startDate, LocalDate endDate, Integer startTime, Integer endTime);

    Long countBySubjectIdAndSchedule_GroupId(Long subjectId, Long groupId);

    List<Lesson> findBySubjectIdAndScheduleIdAndTeacherIdAndRoomId(Long subjectId, Long sheduleId, Long teacherId, Long roomId);
    List<Lesson> findAllBySubjectIdAndScheduleId(Long subjectId, Long sheduleId);

    Lesson findBySubjectIdAndScheduleId(Long subjectId, Long sheduleId);

    List<Lesson> findAllByLessonDateAndSubjectId(LocalDate lessonDate, Long subjectId);

    List<Lesson> findAllByTeacherId(Long teacherId);

    List<Lesson> findByLessonDate(LocalDate lessonDate);

}
