package model.enumerations;

import io.univ.rouen.m2gil.smartclass.core.course.Subject;
import io.univ.rouen.m2gil.smartclass.core.course.SubjectRepository;
import io.univ.rouen.m2gil.smartclass.core.user.Grade;
import io.univ.rouen.m2gil.smartclass.core.user.GradeRepository;
import model.provider.Provider;
import model.provider.RandomProvider;

import java.util.ArrayList;
import java.util.List;

public enum Classes {
    // VALUES
    M2GIL(0.4, 0.7, {0, 2}), M2SSI(0.7, 0.8), M2ITA(0.9, 0.95), L3INFO(0.6, 0.8);


    // CONSTANTS
    /**
     * Aims at simulating the result of a probability law. (Value between 0 and 1)
     */
    private static Provider<Double> probabilityProvider = new RandomProvider(0, 1);


    // ATTRIBUTES
    /**
     * Defines the effective presence rate of a student, for each course given
     * for this class.
     */
    private Provider<Double> provider;


    // CONSTRUCTOR
    private Classes(double a, double b) {
        provider = new RandomProvider(a, b);
    }

    // METHOD
    /**
     * Indicates if a student of this class will be present at the next course, based
     * on the presence stats of this same class.
     */
    public boolean simulatePresence() {
        return probabilityProvider.next() < provider.next();
    }


    // GENERATION
    public static List<Grade> defineAllGrades(GradeRepository<Grade> gradeRepository, SubjectRepository<Subject> subjectRepository) {
        List<Grade> list = new ArrayList<>();
        for (Classes c : Classes.values()) {
            Grade grade = new Grade();
            grade.setTitle(c.name());
        }

        gradeRepository.save(list);
        return list;
    }
}
