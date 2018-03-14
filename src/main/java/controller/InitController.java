package controller;

import io.univ.rouen.m2gil.smartclass.core.course.Course;
import io.univ.rouen.m2gil.smartclass.core.course.Subject;
import io.univ.rouen.m2gil.smartclass.core.course.SubjectRepository;
import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.data.DataRepository;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.*;
import io.univ.rouen.m2gil.smartclass.core.user.Grade;
import io.univ.rouen.m2gil.smartclass.core.user.GradeRepository;
import io.univ.rouen.m2gil.smartclass.core.user.User;
import io.univ.rouen.m2gil.smartclass.core.user.UserRepository;
import model.DaysValueProvider;
import model.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Initialize the database with all generators and their types, in order
 * to specify a source for all produced values.
 */
@RestController
@RequestMapping("/init")
public class InitController {
    // REPOSITORIES
    /**
     * The repository used for the storage of data generators
     */
    @Autowired
    private DataGeneratorRepository<DataGenerator> dataGeneratorRepository;

    /**
     * The repository used for the storage of types
     */
    @Autowired
    private DataGeneratorTypeRepository<DataGeneratorType> typeRepository;

    /**
     * The repository used for data storage
     */
    @Autowired
    private DataRepository<Data> dataRepository;

    /**
     * The repository used for the storage of subjects
     */
    @Autowired
    private SubjectRepository<Subject> subjectRepository;

    /**
     * The repository used for the storage of grades
     */
    @Autowired
    private GradeRepository<Grade> gradeRepository;

    /**
     * The repository used for the storage of all users
     */
    @Autowired
    private UserRepository<User> userRepository;


    // ATTRIBUTES
    private List<Subject> subjects;
    private List<Grade> grades;
    private List<User> students;
    private List<User> teachers;
    private List<Course> courses;


    // REQUESTS
    /**
     * Defines in the DBMS, all initial data (subjects, grades, students, teachers)
     */
    @RequestMapping(method = RequestMethod.GET, path="/initialData")
    public ResponseEntity<?> defineInitialData() {
        // Definition of subjects
        subjects = new ArrayList<>();
        for (String s : Values.SUBJECTS) {
            Subject subject = new Subject(); {
                subject.setLabel(s);
            }
            subjects.add(subject);
        }
        subjectRepository.save(subjects);

        // Definition of grades
        grades = new ArrayList<>();
        for (Object[] tab : Values.GRADES) {
            Grade g = new Grade(); {
                g.setTitle((String) tab[0]);

                List<Subject> temp = new ArrayList<>();
                for (int i : (int[]) tab[3]) temp.add(subjects.get(i));
                g.setSubjects(temp);
            }
            grades.add(g);
        }
        gradeRepository.save(grades);

        // Definition of all students
        students = new ArrayList<>();
        int id = 1;
        for (Object[] tab : Values.STUDENTS) {
            Grade g = grades.get((int) tab[1]);

            for (int k = 0; k < (int) tab[0]; ++k) {
                User u = new User(); {
                    u.setLang("fr");
                    u.setBadgeId("E-99999-" + id);
                    u.setEmail();
                }
                students.add(u);
                ++id;
            }
        }
        userRepository.save(students);

        // Definition of all teachers



        return new ResponseEntity<Object>(HttpStatus.OK);
    }



    /**
     * Defines in the DBMS, all sensors types which will be used.
     */
    @RequestMapping(method = RequestMethod.GET, path="/")
    public ResponseEntity<?> init() {
        long start = System.currentTimeMillis();

        // Definition of types
        DataGeneratorType type = makeType("temperature", "°C", -10, 40);
        typeRepository.save(type);

        // Definition of the data generator
        SmartSensor sensor = makeSensor(type);
        dataGeneratorRepository.save(sensor);

        // Definition of all data for 2 months.
        DaysValueProvider provider = new DaysValueProvider(sensor, -5, 20, 365);
        while (provider.hasNext()) {
            dataRepository.save(provider.next());
        }

        // Success
        String message = String.format(
                "The sensor has been initialized in %d ms !",
                System.currentTimeMillis() - start
        );
        return new ResponseEntity<Object>(message, HttpStatus.OK);
    }


    // TOOLS
    /**
     * Returns a new sensor type object, based on the provided attributes' value
     */
    private DataGeneratorType makeType(String type, String unitMeasure, double min, double max) {
        DataGeneratorType t = new DataGeneratorType(); {
            t.setType(type);
            t.setUnitMeasure(unitMeasure);
            t.setMinValue(min);
            t.setMaxValue(max);
        }

        return t;
    }

    /**
     * Returns a new sensor object, based on the provided attributes' value
     */
    private SmartSensor makeSensor(DataGeneratorType type) {
        SmartSensor s = new SmartSensor(); {
            s.setEnable(true);
            s.setType(type);
            s.setProducingVirtual(true);
            s.setReference("AXKSI");
        }

        return s;
    }

    /**
     * Returns a
     */
    private Subject makeSubject() {
        Subject s = new Subject(); {
            s.setLabel();
        }

        return s;
    }
}
