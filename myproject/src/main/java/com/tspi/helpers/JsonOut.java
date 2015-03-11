/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.helpers;

import org.json.simple.JSONValue;

/**
 *
 * @author noc
 */
public class JsonOut {
    
    public String objectToJson(Object obj)
    {
        return JSONValue.toJSONString(obj);
    }
}
