package model;

import io.univ.rouen.m2gil.smartclass.core.user.Grade;
import io.univ.rouen.m2gil.smartclass.core.user.User;
import model.provider.LimitedProvider;
import model.provider.Provider;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class StudentInitializer extends LimitedProvider<List<User>> {
    // STUDENT DEFINITION
    public static final int[] USER_CATEGORIES_NB = {30, 20, 1, 49};

    // TEACHERS/OVERSEER DEFINITION
    public static final int PROF_NB = 5;
    public static final int OVERSEER_NB = 2;


    // ATTRIBUTES
    private final List<Provider<User>> providers;


    // CONSTRUCTEUR
    public StudentInitializer(List<Grade> grades) {
        providers = new ArrayList<>(); {
            // Student providers
            for (int k = 0; k < USER_CATEGORIES_NB.length; ++k) {

            }
        }
    }

    // GENERATION
    @Override
    protected List<User> generate() {
        for (int k = 0; k < USER_CATEGORIES_NB.length; ++k) {

        }

    }
}
