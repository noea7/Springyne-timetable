package lt.techin.springyne.subject;

import lt.techin.springyne.module.ModuleRepository;
import lt.techin.springyne.room.Room;
import lt.techin.springyne.room.RoomRepository;
import lt.techin.springyne.validationUnits.ModuleUtils;
import lt.techin.springyne.validationUnits.RoomUtils;
import lt.techin.springyne.validationUnits.SubjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import lt.techin.springyne.module.Module;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static lt.techin.springyne.validationUnits.ValidationUtilsNotNull.isValidByName;


@Service
public class SubjectService {

    @Autowired
    private final SubjectRepository subjectRepository;
    @Autowired
    private final ModuleRepository moduleRepository;
    @Autowired
    private final RoomRepository roomRepository;

    private final SubjectUtils subjectUtils;

    private final ModuleUtils moduleUtils;

    private final RoomUtils roomUtils;


    public SubjectService(SubjectRepository subjectRepository,
                          ModuleRepository moduleRepository,
                          RoomRepository roomRepository) {
        this.subjectRepository = subjectRepository;
        this.moduleRepository = moduleRepository;
        this.roomRepository = roomRepository;

        subjectUtils = new SubjectUtils(subjectRepository);
        moduleUtils = new ModuleUtils(moduleRepository);
        roomUtils = new RoomUtils(roomRepository);

    }

        public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Page<Subject> searchByNamePaged(String name, String moduleName, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("deleted").and(Sort.by("name")));

        if(moduleName == null || moduleName.equals("")) {
            if(name == null || name.equals("")) {
                return subjectRepository.findAll(pageable);
            }
            return subjectRepository.findAllByNameIgnoreCaseContaining(name, pageable);
        } else if (name == null ||  name.equals("")){
            return subjectRepository.findAllByModuleNameIgnoreCaseContaining(moduleName, pageable);
        }
        return  subjectRepository.findAllByNameIgnoreCaseContainingAndModuleNameIgnoreCaseContaining(name,moduleName, pageable);
    }

    public Optional<Subject> getById(Long id) {
        return subjectRepository.findById(id);
    }

    public Subject createSubject(Long moduleId, Long roomId, Subject subject) {

        isValidByName(subject.getName());
        subjectUtils.checkSubjectNameUnique(subject.getName());
        if (moduleId != null) {
            subject.setModule(moduleUtils.getModuleById(moduleId));
        }

        if (roomId != null) {
            Room roomToAdd = roomUtils.getRoomById(roomId);
            subject.getRooms().add(roomToAdd);
        }
        return subjectRepository.save(subject);
    }

    public Subject edit(Long subjectId, Subject subject, Long moduleId, Long roomId) {

        isValidByName(subject.getName());
        Subject updatedSubject = subjectUtils.getSubjectById(subjectId);
        if(!updatedSubject.getName().equals(subject.getName())) {
            subjectUtils.checkSubjectNameUnique(subject.getName());
        }

        updatedSubject.setName(subject.getName());
        updatedSubject.setDescription(subject.getDescription());

        if (moduleId != null) {

            updatedSubject.setModule(moduleUtils.getModuleById(moduleId));
        }

        if (roomId != null) {

            updatedSubject.getRooms().add(roomUtils.getRoomById(roomId));

        }

        return subjectRepository.save(updatedSubject);
    }

    @Transactional
    public void deleteRoomFromSubject(Long subjectId, Long roomId) {

        Room roomToRemove = roomUtils.getRoomById(roomId);

        Subject getSubject = subjectUtils.getSubjectById(subjectId);
        getSubject.getRooms().remove(roomToRemove);


    }



    public Subject delete(Long subjectId) {

        Subject existingSubject = subjectUtils.getSubjectById(subjectId);
        if (!existingSubject.isDeleted()) {
            existingSubject.setDeleted(true);
            return subjectRepository.save(existingSubject);
        } else {
            return existingSubject;
        }

    }


    public Subject restore(Long id) {
        var existingSubject = subjectUtils.getSubjectById(id);

        if (existingSubject.isDeleted()) {
            existingSubject.setDeleted(false);
            return subjectRepository.save(existingSubject);
        } else {
            return existingSubject;
        }
    }
    public Subject addModuleToSubject(Long subjectId, Long moduleId) {
        Module moduleToAdd = moduleUtils.getModuleById(moduleId);
        Subject updatedSubject = subjectUtils.getSubjectById(subjectId);
        updatedSubject.setModule(moduleToAdd);
        return subjectRepository.save(updatedSubject);
    }

}