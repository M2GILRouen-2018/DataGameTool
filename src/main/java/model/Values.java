package model;

/**
 * Defines lots of initial data, used for the demo.
 */
public class Values {
    // DATA GAME DEFINITION
    public final static String[] SUBJECTS = {"SPRING", "JAVA", "KOTLIN", "CRYPTO", "TYPESCRIPT", "COMPILATION"};

    /** Grades' array : [Name, min_presency, max_presency, related_subjects] */
    public final static Object[][] GRADES = {
            {"M2GIL", 0.4, 0.7, new int[] {0, 2}},
            {"M2SSI", 0.7, 0.8, new int[] {3, 4, 5}},
            {"M2ITA", 0.9, 0.95, new int[] {4, 5}},
            {"L3INFO", 0.9, 0.95, new int[] {1, 5}}
    };

    /** Students' array : [count, grade] */
    public final static Object[][] STUDENTS = {
            {30, 0},
            {20, 1},
            {1, 2},
            {49, 3}
    };

    /** Teachers' array : [teached subjects] */
    public final static Object[][] TEACHERS = {{0, 1}, {3}, {0, 2}, {4}, {5}};

    /** Courses'array : [DayOfWeek, start, end,  */
    public final static Object[][] COURSES = {
            {},
            {}
    };

    // OTHER VALUES
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
}
