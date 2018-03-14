package model;

import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.DataGenerator;
import model.provider.DataProvider;
import model.provider.LimitedProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Aims at producing lots of value to simulate the data output of a sensor
 * for a given number of days.
 */
public class DaysValueProvider extends LimitedProvider<List<Data>> {
    // CONSTANTES
    /**
     * 24 * 60 minutes in a single day
     */
    public static final int MINUTES_PER_DAY = 24 * 60;

    // ATTRIBUTES
    /**
     * The data provider which is associated to this "sensor" emulator.
     */
    private DataProvider dataProvider;


    // CONSTRUCTOR
    public DaysValueProvider(DataGenerator dataGenerator, double a, double b, int days) {
        super(days);

        dataProvider = new DataProvider(dataGenerator, a, b, days * MINUTES_PER_DAY);
    }

    // TOOL
    @Override
    protected List<Data> generate() {
        List<Data> list = new ArrayList<Data>();

        // Adding all values for a single day.
        for (int k = 0; k < MINUTES_PER_DAY && hasNext() && dataProvider.hasNext(); ++k) {
            list.add(dataProvider.next());
        }

        return list;
    }
}
