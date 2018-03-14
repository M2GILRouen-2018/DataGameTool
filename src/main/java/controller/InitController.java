package controller;

import io.univ.rouen.m2gil.smartclass.core.course.Course;
import io.univ.rouen.m2gil.smartclass.core.course.Subject;
import io.univ.rouen.m2gil.smartclass.core.course.SubjectRepository;
import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.data.DataRepository;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.*;
import io.univ.rouen.m2gil.smartclass.core.user.*;
import model.DaysValueProvider;
import model.Values;
import model.provider.Provider;
import model.provider.ProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<User> overseers;
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
            subjects.add(makeSubject(s));
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
        Provider<String> firstNameProvider = ProviderBuilder.getItemProvider(Values.FIRST_NAMES);
        Provider<String> lastNameProvider = ProviderBuilder.getItemProvider(Values.LAST_NAMES);
        students = new ArrayList<>();
        int id = 1;
        for (Object[] tab : Values.STUDENTS) {
            Grade g = grades.get((int) tab[1]);

            for (int k = 0; k < (int) tab[0]; ++k) {
                User u = new User(); {
                    u.setLang("fr");
                    u.setEnabled(true);
                    u.setBadgeId("E-99999-" + id);
                    u.setRole(Role.STUDENT);
                    u.setGrades(Arrays.asList(g));
                    u.setFirstName(firstNameProvider.next());
                    u.setLastName(lastNameProvider.next());
                    u.setEmail(createEmail(u, id));
                }
                students.add(u);
                ++id;
            }
        }
        userRepository.save(students);

        // Definition of all teachers
        teachers = new ArrayList<>();
        for (Object[] tab : Values.TEACHERS) {
            User u = new User(); {
                u.setLang("fr");
                u.setEnabled(true);
                u.setBadgeId("E-99999-" + id);
                u.setRole(Role.TEACHER);
                u.setFirstName(firstNameProvider.next());
                u.setLastName(lastNameProvider.next());
                u.setEmail(createEmail(u, id));

                List<Subject> temp = new ArrayList<>();
                for (int k = 0; k < tab.length; ++k) {
                    temp.add(subjects.get((int) tab[k]));
                }
                u.setSubjectsRegistered(temp);
            }
            teachers.add(u);
            ++id;
        }
        userRepository.save(teachers);

        return new ResponseEntity<>(HttpStatus.OK);
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
     * Returns a new Subject object
     */
    private Subject makeSubject(String label) {
        Subject s = new Subject(); {
            s.setLabel(label);
        }

        return s;
    }

    /**
     * Create a new email based on a id, a first name and a last name.
     */
    private String createEmail(User u, int id) {
        return String.format(
                "%s.%s%d@gmail.com",
                u.getFirstName().toLowerCase().replace(' ', '_'),
                u.getLastName().toLowerCase().replace(' ', '_'),
                id
        );
    }
}
