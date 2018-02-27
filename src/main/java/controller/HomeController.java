package controller;

import io.univ.rouen.m2gil.smartclass.core.data.Data;
import model.Provider;
import model.RandomProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

@RestController
public class HomeController {
    // REQUETE
    /**
     * Renvoie une donnée "capteur aléatoire comprise entre x et y
     */
    @RequestMapping(method = RequestMethod.GET, path="/data", produces="application/json")
    public ResponseEntity<Data> produceData() {
        Provider<Double> provider = new RandomProvider();
        Data data = new Data(); {
            data.setDateTime(new Date());
            data.setValue(String.format("%.2f", provider.next()));
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}


