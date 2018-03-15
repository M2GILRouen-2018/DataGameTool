package model.provider;

import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.DataGenerator;
import model.Values;
import model.provider.sequence.RangeSequence;

import java.time.LocalDateTime;

/**
 * Aims at producing lots of value to simulate the use of
 * a data generator for a long time.
 */
class DataProvider extends AbstractProvider<Data> {
    // ATTRIBUTES
    /**
     * The data generator which is used in this simulation.
     */
    private final DataGenerator dataGenerator;

    /**
     * The range sequence used in this simulation to produce the data within a key interval
     */
    private final Provider<Double> valueProvider;


    // CONSTRUCTOR
    /**
     * Defines a new simulation based on a given generator and on various parameters
     *
     * @param dataGenerator The generator which is supposed to have produced these values
     * @param valueProvider the provider used to create all values
     */
    public DataProvider(DataGenerator dataGenerator, Provider<Double> valueProvider) {
        this.dataGenerator = dataGenerator;
        this.valueProvider = valueProvider;
    }

    /**
     * Defines a new simulation based on a given generator and on various parameters
     *
     * @param dataGenerator The generator which is supposed to have produced these values
     * @param a one boundary of the range
     * @param b the other
     */
    public DataProvider(DataGenerator dataGenerator, double a, double b) {
        this(dataGenerator, new RangeSequence(a, b));
    }


    // COMMAND
    @Override
    public boolean hasNext() {
        return valueProvider.hasNext();
    }

    @Override
    public void reset(Data last) {
        super.reset(last);
        valueProvider.reset(last == null ? null : Double.valueOf(last.getValue()));
    }

    @Override
    protected Data generate() {
        LocalDateTime dateTime = last() == null ? Values.START : last().getDateTime().plusMinutes(1);
        Data data = new Data(); {
            data.setSourceData(dataGenerator);
            data.setDateTime(dateTime);
            data.setValue(String.format("%.2f", valueProvider.next()).replace(',','.'));
        }

        return data;
    }
}
