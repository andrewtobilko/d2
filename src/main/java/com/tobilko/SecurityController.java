package com.tobilko;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Andrew Tobilko on 10/28/18.
 */
@RestController
public class SecurityController {

    @Autowired
    private SecurityService service;

    @PutMapping("/api/config/url")
    public void changeURL(@RequestParam(name = "value") String newURL) {
        service.setBaseServerURL(newURL);
    }

    @PutMapping("/api/config/algorithm")
    public void changeEncryptionAlgorithm(@RequestParam(name = "value") String al) {
        service.setEncryptionAlgorithm(al);
    }

    @PutMapping("/api/config/encrypt")
    public void changeEncryptResponses(@RequestParam(name = "value") boolean v) {
        service.setEncryptResponses(v);
    }

    @GetMapping("/api/config")
    public SecurityService getConf() {
        return service;
    }

}
