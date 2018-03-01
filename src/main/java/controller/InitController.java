package controller;

import model.entity.DataGenerator;
import repository.DataGeneratorRepository;
import model.entity.DataGeneratorType;
import repository.DataGeneratorTypeRepository;
import model.entity.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    private DataGeneratorRepository dataGeneratorRepository;

    /**
     * The repository used for the storage of types
     */
    @Autowired
    private DataGeneratorTypeRepository typeRepository;


    /**
     * The list of all defined data generators available
     */
    private List<DataGenerator> dataGenerators;

    /**
     * The list of all types available
     */
    private List<DataGeneratorType> types;


    // REQUESTS
    /**
     * Defines in the DBMS, all sensors types which will be used.
     */
    @RequestMapping(method = RequestMethod.GET, path="/")
    public ResponseEntity<?> init() {
        // Definition of types
        ResponseEntity initTypesResponse = initTypes();
        if (initTypesResponse.getStatusCode() != HttpStatus.OK) return initTypesResponse;

        // Definition of data generators
        ResponseEntity initSensorsResponse = initSensors();
        if (initSensorsResponse.getStatusCode() != HttpStatus.OK) return initSensorsResponse;

        // Success
        return new ResponseEntity<Object>("The sensors (and their types) have been initialized !", HttpStatus.OK);
    }


    /**
     * Defines in the DBMS, all sensors types which will be used.
     */
    @RequestMapping(method = RequestMethod.GET, path="/types")
    public ResponseEntity<?> initTypes() {
        // Definition of entities
        typeRepository.save(makeType("Température", "°C", -257, 999));
        typeRepository.save(makeType("Hygrométrie", "%", 0, 100));

        // Success
        types = (List<DataGeneratorType>) typeRepository.findAll();
        return new ResponseEntity<Object>(types, HttpStatus.OK);
    }

    /**
     * Defines in the DBMS, all "real sensors" to use as data's source.
     */
    @RequestMapping(method = RequestMethod.GET, path="/sensors")
    public ResponseEntity<?> initSensors() {
        List<DataGenerator> sensors = new ArrayList<>(); {
            // Definition of entities
            for (int k = 0; k < 4; ++k) sensors.add(makeSensor(types.get(0)));
            sensors.add(2, makeSensor(types.get(1)));
        }

        // Saving in database, then returning all created objects
        dataGenerators = new ArrayList<>();
        for (DataGenerator sensor : sensors) {
            DataGenerator dataGenerator = (DataGenerator) dataGeneratorRepository.save(sensor);
            dataGenerators.add(dataGenerator);
        }
        return new ResponseEntity<Object>(dataGenerators, HttpStatus.OK);
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
    private Sensor makeSensor(DataGeneratorType type) {
        Sensor s = new Sensor(); {
            s.setEnable(true);
            s.setType(type);
            s.setProducingVirtual(true);
        }

        return s;
    }
}
