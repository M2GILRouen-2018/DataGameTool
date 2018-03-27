package service;

import io.univ.rouen.m2gil.smartclass.core.classroom.Classroom;
import io.univ.rouen.m2gil.smartclass.core.classroom.ClassroomRepository;
import io.univ.rouen.m2gil.smartclass.core.course.Course;
import io.univ.rouen.m2gil.smartclass.core.course.CourseRepository;
import io.univ.rouen.m2gil.smartclass.core.course.Subject;
import io.univ.rouen.m2gil.smartclass.core.course.SubjectRepository;
import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.data.DataRepository;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.*;
import io.univ.rouen.m2gil.smartclass.core.presencehistoric.PresenceHistoric;
import io.univ.rouen.m2gil.smartclass.core.presencehistoric.PresenceHistoricRepository;
import io.univ.rouen.m2gil.smartclass.core.user.*;
import model.Values;
import model.provider.Provider;
import model.provider.ProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DataGame's initialization service.
 */
@Service
public class InitService {
    // RANDOM INT
    private static int INT = 0;
    private static final int nextInt() {
        ++INT;
        return INT;
    }

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

    /**
     * The repository used for the storage of all courses
     */
    @Autowired
    private CourseRepository<Course> courseRepository;

    /**
     * The repository used for the storage of all presency tickets.
     */
    @Autowired
    private PresenceHistoricRepository<PresenceHistoric> presenceHistoricRepository;


    // ATTRIBUTES
    private List<Subject> subjects;
    private List<Grade> grades;
    private List<User> students;
    private List<User> teachers;
    private List<User> overseers;
    private List<Course> courses;
    private List<DataGeneratorType> types;
    private List<Classroom> classrooms;
    private List<PresenceHistoric> presencies;


    // REQUESTS
    /**
     * Defines in the DBMS, all initial data (subjects, grades, students, teachers)
     */
    public void demo() {
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

        // Defining all classrooms
        Provider<Double> probabilityProvider = ProviderBuilder.getProbabilityProvider();
        classrooms = new ArrayList<>();
        for (int i = 1; i <= Values.DEMO_BASIC_ROOMS; ++i) {
            Classroom c = new Classroom(); {
                c.setName("U2.2." + (i + 30));
            }
            classrooms.add(c);

        }
        classroomRepository.save(classrooms);

        // Definition of all courses
        courses = new ArrayList<>();
        LocalDate date = Values.START.toLocalDate();
        while (!date.equals(Values.END.toLocalDate())) {
            // Getting params.
            if (Values.COURSES.containsKey(date.getDayOfWeek())) {
                Object[][] params = Values.COURSES.get(date.getDayOfWeek());

                // Building all courses
                for (Object[] tab : params) {
                    Course c = new Course();
                    {
                        int i = ProviderBuilder.getRandomIntProvider(((int[]) tab[6])[0], ((int[]) tab[6])[1]).next();
                        c.setClassroom(classrooms.get(i));
                        c.setStartDate(LocalDateTime.of(date, (LocalTime) tab[1]));
                        c.setEndDate(LocalDateTime.of(date, (LocalTime) tab[2]));
                        c.setSubject(subjects.get((int) tab[3]));
                        c.setLabel(c.getSubject().getLabel() + " - " + tab[4]);
                    }
                    courses.add(c);
                }
            }

            // Taking the next day
            date = date.plusDays(1);
        }
        courseRepository.save(courses);

        // "Logging" about time spent
        System.out.println(String.format(
                "Courses initialised in %d ms", System.currentTimeMillis() - start)
        );

        // Presencies definition's loop
        start = System.currentTimeMillis();
        Provider<Integer> deltaProvider = ProviderBuilder.getRandomIntProvider(-Values.PRESENCY_DELTA, Values.PRESENCY_DELTA);
        presencies = new ArrayList<>();

        // Defining all users for all grades.
        Map<Grade, List<User>> promotions = new HashMap<>(); {
            for (Grade g : grades) {
                promotions.put(g, students.stream().filter(s -> s.getGrades().contains(g)).collect(Collectors.toList()));
            }
        }

        // Defining all courses' metadata
        List<Object[]> coursesMetaData = new ArrayList<>(); {
            for (Object[][] tab : Values.COURSES.values()) {
                for (Object[] attr : tab) coursesMetaData.add(attr);
            }
        }
        List<List<Grade>> gradesMetaData = new ArrayList<>(); {
            for (Object[] courseMetaData : coursesMetaData) {
                gradesMetaData.add(
                        Arrays.stream((int[]) courseMetaData[0])
                        .mapToObj(i -> grades.get(i))
                        .collect(Collectors.toList())
                );
            }
        }
        int index = 0;
        int n = coursesMetaData.size();

        // Presency loop
        for (Course c : courses) {
            for (Grade g : gradesMetaData.get(index)) {
                for (User student : promotions.get(g)) {
                    int beginDelta = deltaProvider.next();
                    int endDelta = deltaProvider.next();
                    LocalDateTime begin = beginDelta >= 0 ? c.getStartDate().plusMinutes(beginDelta) : c.getStartDate().minusMinutes(beginDelta);
                    LocalDateTime end = endDelta >= 0 ? c.getEndDate().plusMinutes(endDelta) : c.getEndDate().minusMinutes(endDelta);

                    // Creating presency ticket (if the student is present)
                    if (probabilityProvider.next() < Values.PRESENCY_RATE) {
                        presencies.add(makePresenceTicket(student, c.getClassroom(), begin, end));
                    }
                }
            }

            // Adding course's teacher
            User teacher = teachers.get((Integer) coursesMetaData.get(index)[5]);
            presencies.add(makePresenceTicket(teacher, c.getClassroom(), c.getStartDate(), c.getEndDate()));

            // Handling teachers
            index = (index + 1) % n;
        }
        presenceHistoricRepository.save(presencies);

        // "Logging" about time spent
        System.out.println(String.format(
                "Presency data initialised in %d ms", System.currentTimeMillis() - start)
        );

        // End of demo
        System.out.println(String.format(
                "All demo meta-data initialised in %d ms", System.currentTimeMillis() - start)
        );
    }

    /**
     * Defines in the DBMS, all sensors (and their data) for all classrooms.
     */
    public void fill() {
        fill(Values.DEMO_DAYS);
    }

    /**
     * Defines in the DBMS, all sensors (and their data) for all classrooms.
     * The data will be defined for a given amount of days.
     */
    public void fill(long days) {
        for (Classroom c : classroomRepository.findAll()) {
            fill(c.getId(), days);
        }
    }

    /**
     * Defines in the DBMS, all sensors (and their data) located in a given room,
     * for a given amount of days.
     */
    public void fill(long id, long days) {
        long start = System.currentTimeMillis();
        Classroom c = classroomRepository.findOne(id);
        types = typeRepository.findAll();
        Provider<Double> probabilityProvider = ProviderBuilder.getProbabilityProvider();

        // Defining associated sensors
        System.out.println("--------- " + c.getName() + " ---------");
        for (int k = 0; k < 3; ++k) {
            // Simulate the lack of this type of sensor in a given class
            if (probabilityProvider.next() > Values.DEMO_SKIP_SENSOR_CHANCE) {
                do {
                    long s2 = System.currentTimeMillis();
                    DataGenerator dg = makeSensor(c, types.get(k));
                    dataGeneratorRepository.save(dg);

                    List<Data> datas = produceData(dg, k, days);
                    long s3 = System.currentTimeMillis();
                    System.out.println(k + " >> Data produced in " + (s3 - s2) + "ms.");
                    dataRepository.save(datas);
                    System.out.println(k + " >> Data stored in " + (System.currentTimeMillis() - s3) + "ms.");

                    // Simulate an additionnal sensor of this type ?
                } while (probabilityProvider.next() < Values.DEMO_ADDITIONNAL_SENSOR_CHANCE);
            }
        }

        // End of demo
        String message = String.format("Sensor data initialised in %d ms", System.currentTimeMillis() - start);
        System.out.println(message);
    }

    /**
     * Remove all initialized data.
     */
    public void clear() {
        presenceHistoricRepository.deleteAll();
        courseRepository.deleteAll();
        dataRepository.deleteAll();
        dataGeneratorRepository.deleteAll();
        classroomRepository.deleteAll();
        typeRepository.deleteAll();
        userRepository.deleteAll();
        gradeRepository.deleteAll();
        subjectRepository.deleteAll();
    }

    // TOOLS
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
                "%s.%s%d@smartclass.com",
                u.getFirstName().toLowerCase().replace(' ', '_'),
                u.getLastName().toLowerCase().replace(' ', '_'),
                id
        );
    }

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
    private SmartSensor makeSensor(Classroom c, DataGeneratorType type) {
        SmartSensor s = new SmartSensor(); {
            s.setClassroom(c);
            s.setEnable(true);
            s.setType(type);
            s.setProducingVirtual(true);
            s.setReference("AXKSI-" + nextInt());
        }

        return s;
    }

    /**
     * Returns a new presency ticket, attesting the presency of a user for a given interval of time in a given room.
     */
    private PresenceHistoric makePresenceTicket(User user, Classroom classroom, LocalDateTime begin, LocalDateTime end) {
        PresenceHistoric presenceHistoric = new PresenceHistoric(); {
            presenceHistoric.setUser(user);
            presenceHistoric.setClassroom(classroom);
            presenceHistoric.setBegin(begin);
            presenceHistoric.setEnd(end);
        }

        return presenceHistoric;
    }

    /**
     * Produce all data for a given sensor.
     */
    private List<Data> produceData(DataGenerator dg, int typeId, long days) {
        Provider<Data> dataProvider = null;

        // Temp and Hygro.
        if (0 <= typeId && typeId < 2) {
            // Generation parameters
            int n = (int) (Math.random() * Values.VARIANCES[typeId].length);
            double min = (double) Values.VARIANCES[typeId][n][0];
            double max = (double) Values.VARIANCES[typeId][n][1];

            // Creating associated data provider
            dataProvider = ProviderBuilder.getDataProvider(dg, min, max);
        }
        // Light
        else if (typeId == 2) {
            // Generation parameters
            LocalTime lightUp = (LocalTime) Values.VARIANCES[2][0][0];
            LocalTime lightDown = (LocalTime) Values.VARIANCES[2][0][1];

            // Creating associated data provider
            dataProvider = ProviderBuilder.getLightProvider(dg, lightUp, lightDown);
        } else {
            throw new AssertionError();
        }

        // Creating data for a given number of days
        Provider<List<Data>> dataCollector = ProviderBuilder.collector(dataProvider,60 * 24);
        List<Data> datas = new LinkedList<>();
        for (int day = 0; day < days; ++day) {
            datas.addAll(dataCollector.next());
            dataProvider.reset();
        }

        return datas;
    }
}
