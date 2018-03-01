package model.provider;

import model.entity.DataGenerator;
import model.entity.DataModel;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Aims at producing Data
 */
public class DataProvider extends AbstractProvider<DataModel> {
    // ATTRIBUTE
    /**
     * The random provider used to provide decimal values.
     */
    private Provider<Double> decimalProvider;


    /**
     * The data generator which is supposed to be the source of this
     * data.
     */
    private DataGenerator dataGenerator;


    // CONSTRUCTEUR

    /**
     * Builds a new data provider based on a given data generator and a interval
     * of decimal values to specify the range of values which produced pieces
     * of data can have.
     *
     * @param dataGenerator The data generator linked to the production of this piece of data
     * @param min Values range's min
     * @param max Values range's max
     */
    public DataProvider(DataGenerator dataGenerator, double min, double max) {
        decimalProvider = new RandomProvider(min, max);
        this.dataGenerator = dataGenerator;
    }
    public DataProvider(DataGenerator dataGenerator) {
        this(dataGenerator, 0, 100);
    }


    @Override
    protected DataModel generate() {
        DataModel data = new DataModel(); {
            data.setValue(String.format("%.2f", decimalProvider.next()));
            data.setSourceData(dataGenerator);
            data.setDateTime(LocalDateTime.now(Clock.systemDefaultZone()));
        }

        return data;
    }
}
