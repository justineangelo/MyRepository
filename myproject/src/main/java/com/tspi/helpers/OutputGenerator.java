/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.helpers;

import java.util.HashMap;

/**
 *
 * @author noc
 */
public class OutputGenerator extends JsonOut{
    
    public static String generateError500(String desc)
    {
        OutputGenerator outGen= new OutputGenerator();
        return outGen.objectToJson(outGen.generateResponse("500", desc));
    }
    
    public static String generateError404(String desc)
    {
        OutputGenerator outGen= new OutputGenerator();
        return outGen.objectToJson(outGen.generateResponse("404", desc));
    }
    
    public static String generateFailCustom(String status_code, String desc)
    {
        OutputGenerator outGen= new OutputGenerator();
        return outGen.objectToJson(outGen.generateResponse(status_code, desc));
    }
    
    public static String generateSuccess(String desc)
    {
        OutputGenerator outGen= new OutputGenerator();
        return outGen.objectToJson(outGen.generateResponse("100", desc));
    }
    
    
    
    private HashMap<String, String> generateResponse(String status_code, String status_desc){
        HashMap<String, String> result = new HashMap<>();
        result.put("status_code", status_code);
        result.put("status_desc", status_desc);
        return result;
    }
}
