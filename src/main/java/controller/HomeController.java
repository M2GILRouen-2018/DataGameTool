package controller;

import io.univ.rouen.m2gil.smartclass.core.data.Data;
import model.provider.Provider;
import model.provider.RandomProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

@RestController
public class HomeController {
    // REQUETE
    @RequestMapping(method = RequestMethod.GET, path="/")
    public String home() {
        return "Hello World !";
    }
}


