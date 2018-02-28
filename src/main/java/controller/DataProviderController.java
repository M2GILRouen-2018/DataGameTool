package controller;

import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.data.DataRepository;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.DataGenerator;
import model.Provider;
import model.RandomProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

@RestController
public class DataProviderController {
    // ATTRIBUT
    @Autowired
    private DataRepository<Data> repository;


    // REQUETE
    /**
     * Returns a fake and simple generated "data"
     */
    @RequestMapping(method = RequestMethod.GET, path="/data", produces="application/json")
    public ResponseEntity<Data> index() {
        Data data = generateData(0, 100);
        return new ResponseEntity<Data>(data, HttpStatus.OK);
    }


    /**
     * Produces a new piece of data, which is instantly stored in the database,
     * and returned by this method.
     */
    @RequestMapping(method = RequestMethod.POST, path="/data", produces="application/json")
    public ResponseEntity<Data> persist() {
        Data data = generateData(0, 100);
        repository.save(data);

        return new ResponseEntity<Data>(data, HttpStatus.OK);
    }


    /**
     * Read a piece of data stored in the database, based on an identification number
     */
    @RequestMapping(method = RequestMethod.GET, path="/data/read/{id}", produces="application/json")
    public ResponseEntity<?> read(@PathVariable("id") long id) {
        Data data = repository.findOne(id);

        if (data == null) {
            return new ResponseEntity<>("Identifiant non trouvé", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    // TOOL
    /**
     * Returns a simple data sample which has a value in the interval ]a, b[
     */
    private Data generateData(double a, double b) {
        Provider<Double> provider = new RandomProvider(a, b);
        Data data = new Data(); {
            data.setDateTime(new Date());
            data.setValue(String.format("%.2f", provider.next()));
        }

        return data;
    }
}


