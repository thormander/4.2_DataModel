package edu.baylor.cs.se.hibernate.controller;

import edu.baylor.cs.se.hibernate.services.PersonRepository;
import edu.baylor.cs.se.hibernate.services.SuperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


//Ignore this as it is Spring and not Java EE (Jax-RS) controller
@RestController
public class MyController {

    private SuperRepository superRepository;

    //you should generally favour constructor/setter injection over field injection
    @Autowired
    public MyController(SuperRepository superRepository,PersonRepository personRepository){
        this.superRepository = superRepository;
    }

    //very bad practise - using GET method to insert something to DB
    @RequestMapping(value = "/populate", method = RequestMethod.GET)
    public ResponseEntity populate(){
        superRepository.populate();

        return new ResponseEntity(HttpStatus.OK);
    }

}
