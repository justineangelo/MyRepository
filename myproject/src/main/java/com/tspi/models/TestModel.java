/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.models;

import com.tspi.template.ModelTemplate;
import java.util.HashMap;

/**
 *
 * @author noc
 */
public class TestModel extends ModelTemplate{
    private ModelTemplate mt, mt2;
    public TestModel(){
        //constructor
        this.loadDatabase("samplepostgre");//sample
        mt = this.loadDatabase("samplepostgre", true);//sample for multiple access
        mt2 = this.loadDatabase("samplepostgre", true);// new sample for multiple access
    }
    public void testDBConnection(){
        this.setDefaultQuery("SELECT now()");
        this.startConnection();
        Object obj = this.outputResult();
        this.loggerDebug(obj.toString());
    }
    public void selectSample(){
        HashMap<String, String> where = new HashMap<>();
        where.put("id", "1110");
        mt.dbWhere(where);
        mt.dbSelect("name, company, address");
        mt.dbLimit(4);
        mt.dbGet("\"CORE\".\"USERS\"");
        
        where.clear();
        where.put("id","1112");
        mt2.dbWhere(where);
        mt2.dbSelect("name,company,address");
        mt2.dbLimit(1);
        mt2.dbGet("\"CORE\".\"USERS\"");
        
        where.clear();
        where.put("id", "1111");
        this.dbWhere(where);
        this.dbSelect("name, company, address");
        this.dbLimit(4);
        this.dbGet("\"CORE\".\"USERS\"");
    }
}
