package model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines lots of initial data, used for the demo.
 */
public class Values {
    // DEMO DEFINITION
    public static final long DEMO_DAYS = 365;

    public static final long DEMO_BASIC_ROOMS = 20;

    public static final double DEMO_ADDITIONNAL_SENSOR_CHANCE = 0.1;

    public static final double DEMO_SKIP_SENSOR_CHANCE = 0.06;


    // DATA GAME DEFINITION
    public static final String[] SUBJECTS = {"SPRING", "JAVA", "KOTLIN", "CRYPTO", "TYPESCRIPT", "COMPILATION"};

    /** Grades' array : [Name, min_presency, max_presency, related_subjects] */
    public static final Object[][] GRADES = {
            {"M2GIL", 0.4, 0.7, new int[] {0, 2}},
            {"M2SSI", 0.7, 0.8, new int[] {3, 4, 5}},
            {"M2ITA", 0.9, 0.95, new int[] {4, 5}},
            {"L3INFO", 0.9, 0.95, new int[] {1, 5}}
    };

    /** Students' array : [count, grade] */
    public static final Object[][] STUDENTS = {
            {30, 0},
            {20, 1},
            {1, 2},
            {49, 3}
    };

    /** Teachers' array : [teached subjects] */
    public static final Object[][] TEACHERS = {{0, 1}, {3}, {0, 2}, {4}, {5}};

    /** Overseer' count */
    public static final int OVERSEER_NB = 3;

    /** Data generators' types [name, unit, min, max] */
    public static final Object[][] TYPES = {
            {"temperature", "°C", -20., 60.},
            {"hygro", "%", 0., 100.},
            {"light", "%", 0., 100.},
    };

    /** Data generators' variances : [type][min, max] */
    public static final Object[][][] VARIANCES = {
            {{20., 25.}, {21., 24.}, {20., 23.}, {21., 22.}, {21., 25.}, {19., 23.}},
            {{20., 40.}, {15., 45.}, {15., 40.}, {20., 35.}, {25., 35.}},
            {{LocalTime.of(8,0), LocalTime.of(18,0)}}
    };
    public static  final int LIGHT_STEP = 45;

    /** Courses' infos : [DayOfWeek][Grades, start, end, subject, label, teacher, room choice (2)] */
    public static final Map<DayOfWeek, Object[][]> COURSES = new HashMap<>();
    static {
        COURSES.put(DayOfWeek.MONDAY, new Object[][] {
                {new int[] {0, 1}, LocalTime.of(8, 0), LocalTime.of(10, 0), 0, "CM", 2, new int[] {0, 4}},
                {new int[] {3}, LocalTime.of(9, 0), LocalTime.of(12, 0), 1, "CM", 0, new int[] {5, 9}},
                {new int[] {1}, LocalTime.of(10, 0), LocalTime.of(12, 0), 3, "CM", 1, new int[] {10, 13}},
                {new int[] {1, 2}, LocalTime.of(13, 0), LocalTime.of(16, 0), 4, "CM", 3, new int[] {0, 4}},
                {new int[] {3}, LocalTime.of(13, 0), LocalTime.of(15, 0), 5, "CM", 4, new int[] {18, 18}},
                {new int[] {0}, LocalTime.of(14, 0), LocalTime.of(17, 0), 2, "CM", 2, new int[] {5, 9}}
        });
    }


    // OTHER VALUES
    /**
     * The average light value during night.
     */
    public static final double NIGHT_LIGHT = 0.05;

    /**
     * The average light value during the day.
     */
    public static final double DAY_LIGHT = 0.85;

    /**
     * Defines a list of available first names.
     */
    public static final String[] FIRST_NAMES = {
            "Amine", "Camille", "Loïck", "Mohamed", "Geoffrey", "Said",
            "Emilien", "Juba", "Nicolas", "Corentin", "Jordan", "Islam",
            "Yacine", "Ghiles", "Ilyass", "Ali", "Franck", "Guillaume"
    };

    /**
     * Defines a list of available last names.
     */
    public static final String[] LAST_NAMES = {
            "SIDOTMANE", "LEPLUMEY", "LEMARCHAND", "TAKTOUKH", "SPAUR", "BENAOUICHA",
            "LEROUX", "TIDAF", "GILLE", "LE GUEN", "BAUDIN", "GUETTOUCHE",
            "HOUMOR", "FEGHOUL", "FARSAL", "BEN HMIDA", "CARON", "NAIMI"
    };

    /**
     * The date used to start all scenariis (01/01/2018, 00:00)
     */
    public static final LocalDateTime START = LocalDateTime.of(2018, 1, 1, 0, 0);
}
