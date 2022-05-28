package com.calebe.springweb.controller;

import com.calebe.springweb.service.AddressRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressRetriever retriever;

    @RequestMapping(path = "/{who}", method = RequestMethod.GET)
    public ResponseEntity<?> getAddress(@PathVariable("who") String who) {
        String address = retriever.getAddress(who);
        if(address == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(address);
    }
}
