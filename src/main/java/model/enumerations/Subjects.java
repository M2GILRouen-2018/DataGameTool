package model.enumerations;


import io.univ.rouen.m2gil.smartclass.core.course.Subject;
import io.univ.rouen.m2gil.smartclass.core.course.SubjectRepository;

import java.util.ArrayList;
import java.util.List;

public enum Subjects {
    SPRING, JAVA, KOTLIN, CRYPTO, TYPESCRIPT, COMPILATION;

    // GENERATION
    public static List<Subject> defineAllSubjects(SubjectRepository<Subject> subjectRepository) {
        List<Subject> list = new ArrayList<>();
        for (Subjects s : Subjects.values()) {
            Subject subject = new Subject(); {
                subject.setLabel(s.name());
            }
        }

        subjectRepository.save(list);
        return list;
    }
}
