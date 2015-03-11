/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author noc
 */

@RestController
@RequestMapping("/xml")
public class SubController {
    
    @RequestMapping(value = "/api.xml", method = RequestMethod.GET, produces = "application/xml")
    public String getXml(){
        return "Hello";
    }
}
