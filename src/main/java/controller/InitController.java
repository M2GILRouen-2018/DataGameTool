package controller;

import io.univ.rouen.m2gil.smartclass.core.classroom.Classroom;
import io.univ.rouen.m2gil.smartclass.core.classroom.ClassroomRepository;
import io.univ.rouen.m2gil.smartclass.core.course.Course;
import io.univ.rouen.m2gil.smartclass.core.course.Subject;
import io.univ.rouen.m2gil.smartclass.core.course.SubjectRepository;
import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.data.DataRepository;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.*;
import io.univ.rouen.m2gil.smartclass.core.user.*;
import model.Values;
import model.provider.Provider;
import model.provider.ProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
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

    /**
     * The repository used for the storage of all classrooms
     */
    @Autowired
    private ClassroomRepository<Classroom> classroomRepository;


    // ATTRIBUTES
    private List<Subject> subjects;
    private List<Grade> grades;
    private List<User> students;
    private List<User> teachers;
    private List<User> overseers;
    private List<Course> courses;
    private List<DataGeneratorType> types;
    private List<Classroom> classrooms;


    // REQUESTS
    /**
     * Defines in the DBMS, all initial data (subjects, grades, students, teachers)
     */
    @RequestMapping(method = RequestMethod.GET, path="/demo/{days}")
    public ResponseEntity<?> demo(@PathVariable Long days) {
        if (days == null) days = Values.DEMO_DAYS;
        long start = System.currentTimeMillis();

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
                u.setBadgeId("P-67849-" + id);
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

        // Definition of all overseers
        overseers = new ArrayList<>();
        for (int count = 0; count < Values.OVERSEER_NB; ++count) {
            User u = new User(); {
                u.setLang("fr");
                u.setEnabled(true);
                u.setBadgeId("S-34512-" + id);
                u.setRole(Role.SUPERVISOR);
                u.setFirstName(firstNameProvider.next());
                u.setLastName(lastNameProvider.next());
                u.setEmail(createEmail(u, id));
            }
            overseers.add(u);
            ++id;
        }
        userRepository.save(overseers);

        // Definition of all data generator types
        types = new ArrayList<>();
        for (Object[] tab : Values.TYPES) {
            DataGeneratorType t = makeType(
                    (String) tab[0], (String) tab[1],
                    (Double) tab[2], (Double) tab[3]
            );
            types.add(t);
        }
        typeRepository.save(types);

        for (int i = 1; i <= 1; ++i) {
            // Defining classroom
            Classroom c = new Classroom(); {
                c.setName("U2.2." + (i + 30));
            }
            classroomRepository.save(c);

            // Defining associated temperature/hygrometry data generator
            for (int k = 0; k < 2; ++k) {
                DataGenerator dg = makeSensor(types.get(k));
                dg.setClassroom(c);
                dataGeneratorRepository.save(dg);

                // Creating a new data type
                int n = (int) (Math.random() * Values.VARIANCES[k].length);
                double min = (double) Values.VARIANCES[k][n][0];
                double max = (double) Values.VARIANCES[k][n][1];

                Provider<List<Data>> dataCollector = ProviderBuilder.collector(
                        ProviderBuilder.getDataProvider(dg, min, max),
                        60 * 24
                );

                // Creating data for a given number of days
                for (int day = 0; day < 7; ++day) {
                    dataRepository.save(dataCollector.next());
                }
            }

            // Defining associated luminosity data generator
            DataGenerator dg = makeSensor(types.get(2));
            dg.setClassroom(c);
            dataGeneratorRepository.save(dg);

            // Creating a new dataProvider to create light levels values
            LocalTime lightUp = (LocalTime) Values.VARIANCES[2][0][0];
            LocalTime lightDown = (LocalTime) Values.VARIANCES[2][0][1];

            int sunrise = lightUp.getHour() * 60 + lightUp.getMinute();
            int sunset = lightDown.getHour() * 60 + lightDown.getMinute();
            int step = 45;


            // Building data provider
            Provider<Double> dvp = ProviderBuilder.compose(
                    ProviderBuilder.limit(
                            ProviderBuilder.getRangeSequence(0.95 * Values.NIGHT_LIGHT, 1.05 * Values.NIGHT_LIGHT),
                            sunrise - step
                    ),
                    ProviderBuilder.limit(
                            ProviderBuilder.getLinearSegmentProvider(2 * step, Values.NIGHT_LIGHT, Values.DAY_LIGHT),
                            2*step
                    ),
                    ProviderBuilder.limit(
                            ProviderBuilder.getRangeSequence(0.95 * Values.DAY_LIGHT, 1.05 * Values.DAY_LIGHT),
                            sunset - (sunrise + step) - step
                    ),
                    ProviderBuilder.limit(
                            ProviderBuilder.getLinearSegmentProvider(2 * step, Values.DAY_LIGHT, Values.NIGHT_LIGHT),
                            2 * step
                    ),
                    ProviderBuilder.limit(
                            ProviderBuilder.getRangeSequence(0.95 * Values.NIGHT_LIGHT, 1.05 * Values.NIGHT_LIGHT),
                            60 * 24 - (sunset + step)
                    )
            );

            // Collecting daily values from this provider
            Provider<List<Data>> dayLightCollector = ProviderBuilder.collector(
                    ProviderBuilder.getDataProvider(dg, dvp),
                    60 * 24
            );

            // Creating data for a given number of days
            for (int day = 0; day < days; ++day) {
                dataRepository.save(dayLightCollector.next());
                dvp.reset();
            }
        }

        // End of demo
        String message = String.format("Initialised in %d ms", System.currentTimeMillis() - start);
        return new ResponseEntity<>(message, HttpStatus.OK);
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
