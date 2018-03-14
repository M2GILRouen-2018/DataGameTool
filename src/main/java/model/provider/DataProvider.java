package model.provider;

import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.DataGenerator;
import model.provider.sequence.RangeSequence;

import java.time.LocalDateTime;

/**
 * Aims at producing lots of value to simulate the use of
 * a data generator for a long time.
 */
class DataProvider extends LimitedProvider<Data> {
    // CONSTANTE
    /**
     * The date used to start all scenariis (01/01/2018, 00:00)
     */
    public static final LocalDateTime START = LocalDateTime.of(2018, 1, 1, 0, 0);


    // ATTRIBUTES
    /**
     * The data generator which is used in this simulation.
     */
    private final DataGenerator dataGenerator;

    /**
     * The range sequence used in this simulation to produce the data within a key interval
     */
    private final RangeSequence rangeSequence;


    // CONSTRUCTOR
    /**
     * Defines a new simulation based on a given generator and on various parameters
     *
     * @param dataGenerator The generator which is supposed to have produced these values
     * @param a one boundary of the range
     * @param b the other
     * @param limit The number of values to produce.
     */
    public DataProvider(DataGenerator dataGenerator, double a, double b, int limit) {
        super(limit);
        this.dataGenerator = dataGenerator;
        this.rangeSequence = new RangeSequence(a, b);
    }


    // COMMAND
    @Override
    protected Data generate() {
        LocalDateTime dateTime = last() == null ? START : last().getDateTime().plusMinutes(1);
        Data data = new Data(); {
            data.setSourceData(dataGenerator);
            data.setDateTime(dateTime);
            data.setValue(String.format("%.2f", rangeSequence.next()).replace(',','.'));
        }

        return data;
    }
}
