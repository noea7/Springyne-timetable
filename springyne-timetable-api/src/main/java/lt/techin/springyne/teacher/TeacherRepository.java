package lt.techin.springyne.teacher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    List<Teacher> findAllByOrderByDeletedAscIdAsc();

    Page<Teacher> findAllByNameIgnoreCaseContainingAndShiftIdAndSubjects_Id(String name, Long shiftId, Long SubjectId, Pageable pageable);
    Page<Teacher> findAllByNameIgnoreCaseContaining(String name, Pageable pageable);

    Page<Teacher> findAllByNameIgnoreCaseContainingAndShiftId(String name, Long shiftId, Pageable pageable);
    Page<Teacher> findAllByNameIgnoreCaseContainingAndSubjects_Id(String name, Long SubjectId, Pageable pageable);

    List<Teacher> findBySubjects_IdAndShift_StartsLessThanEqualAndShift_EndsGreaterThanEqual(Long subjectId, Integer startTime,
                                                                                             Integer endTime);

}
