package controller;

import io.univ.rouen.m2gil.smartclass.core.data.DataRepository;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.DataGenerator;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.DataGeneratorRepository;
import model.entity.DataModel;
import model.provider.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Provides an interface in order to visualise the process of data generation
 * on simple samples.
 *
 * > /scenario/ : Plays all data scenarios defined for the demo (planned in 02/03)
 */
@RestController
@RequestMapping("/scenario")
public class DataScenarioController {
    // GLOBAL VALUES
    /**
     * The values expected by this scenario (in an array (min, max, values_nb))
     */
    private static Object[][] SCENARIO_PARAMETERS = {
            {-5.0, 5.0, 40000},
            {-5.0, 5.0, 8000},
            {20.0, 40.0, 1500},
            {-20.0, 10.0, 1500}
    };


    // ATTRIBUTES
    /**
     * The repository used to read all available generators
     */
    @Autowired
    private DataGeneratorRepository dataGeneratorRepository;

    /**
     * The repository used for the storage of types
     */
    @Autowired
    private DataRepository<DataModel> dataRepository;


    // METHODS
    /**
     * Plays the scenario used to define all required data.
     */
    @RequestMapping(method = RequestMethod.GET, path="/")
    public ResponseEntity<?> play() {
        // Récupération des capteurs
        List<DataGenerator> dataGenerators = (List<DataGenerator>) dataGeneratorRepository.findAll();
        if (dataGenerators == null || dataGenerators.size() < SCENARIO_PARAMETERS.length) {
            return new ResponseEntity<Object>("Les capteurs ne sont pas définis...", HttpStatus.NOT_ACCEPTABLE);
        }

        // Playing all scenariis
        for (int k = 0; k < SCENARIO_PARAMETERS.length; ++k) {
            // Scenario creation
            Object[] parameters = SCENARIO_PARAMETERS[k];
            DataGenerator dataGenerator = dataGenerators.get(k);
            DataProvider dataProvider = new DataProvider(
                    dataGenerator,
                    (double) parameters[0],
                    (double) parameters[1],
                    (int) parameters[2]
            );

            // Playing scenario for data generator k.
            while (dataProvider.hasNext()) {
                DataModel d = dataProvider.next();

                // DEBUG
                if (dataProvider.count() % 500 == 0) System.err.println("In progress : " + dataProvider.count());

                dataRepository.save(d);
            }
        }

        return new ResponseEntity<Object>("All datas have been generated !", HttpStatus.OK);
    }
}
