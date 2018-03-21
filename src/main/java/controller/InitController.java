package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.InitService;

/**
 * REST Controller used to interact with the initialization service.
 */
@RestController
public class InitController {
    // REPOSITORIES
    /**
     * The repository used for the storage of data generators
     */
    @Autowired
    private InitService initService;


    // REQUESTS
    /**
     * Defines in the DBMS, all initial data (subjects, grades, students, teachers)
     */
    @RequestMapping(method = RequestMethod.GET, path="/demo")
    public ResponseEntity<?> demo() {
        return initService.demo();
    }

    /**
     * Defines in the DBMS, all initial data (subjects, grades, students, teachers)
     */
    @RequestMapping(method = RequestMethod.GET, path="/demo/{days}")
    public ResponseEntity<?> demo(@PathVariable Long days) {
        return days == null ? initService.demo() : initService.demo(days);
    }

    /**
     * Defines in the DBMS, all sensors (and their data) for all classrooms.
     */
    @RequestMapping(method = RequestMethod.GET, path="/fill")
    public ResponseEntity<?> fill() {
        return initService.fill();
    }

    /**
     * Defines in the DBMS, all sensors (and their data) located in a given room
     */
    @RequestMapping(method = RequestMethod.GET, path="/fill/{id}")
    public ResponseEntity<?> fill(@PathVariable Long id) {
        return id == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : initService.fill(id);
    }

    /**
     * Defines in the DBMS, all sensors (and their data) located in a given room,
     * for a given amount of days.
     */
    @RequestMapping(method = RequestMethod.GET, path="/fill/{id}/{days}")
    public ResponseEntity<?> fill(@PathVariable Long id, @PathVariable Long days) {
        if (id == null || days == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return initService.fill(id, days);
    }

    /**
     * Remove all defined data form the DBMS
     */
    @DeleteMapping(path = "/")
    public ResponseEntity<?> clear() {
        initService.clear();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
