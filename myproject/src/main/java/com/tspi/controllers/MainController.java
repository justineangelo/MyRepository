/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tspi.models.StoreModel;
import com.tspi.models.TestModel;
import java.util.ArrayList;
import java.util.HashMap;

import com.tspi.helpers.Store;
import org.springframework.web.servlet.ModelAndView;
import com.tspi.helpers.RequestHelper;
import com.tspi.template.ControllerTemplate;

import org.json.simple.JSONValue;
import org.apache.log4j.Logger;


/**
 *
 * @author noc
 */

@RestController
@RequestMapping("/service")
public class MainController extends ControllerTemplate implements IMainController{
    
    private TestModel tm = null;
    private StoreModel sm = null;
    private static final Logger logger = Logger.getLogger(MainController.class);
    
    public MainController(){
        this.loggerInfo("LOAD CONSTRUCTOR FOR MainController");
        this.tm = new TestModel();
        this.sm = new StoreModel();
    }
    
    
    @Override
    @RequestMapping(value = "/forceJson", method = RequestMethod.GET, produces = "application/json")
    public String returnJsonString(){
        ArrayList<HashMap> ret = new ArrayList<>();
        int c = 10;
        for (int i = 0; i < c; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(String.valueOf(i), String.valueOf(i));
            ret.add(map);
        }
        return JSONValue.toJSONString(ret);
    }
    
    @Override
    @RequestMapping(value = "/parsedPage", method = RequestMethod.GET)
    public ModelAndView showParsedPage(){
        logger.info("showParsedPage");
        String s;
        RequestHelper rh = new RequestHelper();
        rh.setStringURI("http://localhost:8080/myproject/service/eStore/api?username=username1&itemname=bag");
//        rh.setStringURI("http://www.w3schools.com/xml/note.xml");
        rh.setRequestProperty(RequestHelper.RequestProperty.REST);
//        rh.setRequestProperty(RequestHelper.RequestProperty.SOAP);
        rh.setRequestMethod(RequestHelper.RequestMethod.GET);
//        HashMap<String, String> postData = new HashMap<>();
//        postData.put("username", "username2");
//        postData.put("itemname", "bag");
//        rh.setPostData(postData);
//        rh.setRequestMethod(RequestHelper.RequestMethod.POST);
        Object obj = rh.requestStart();
        HashMap<String, String> map = (HashMap)obj;
        
//        s = rh.getRawResponseString();
        s= "";
        ModelAndView mv = new ModelAndView("indexParsed");
        mv.addObject("data",s + rh.getErrorDesciption() + map.get("statusDesc"));
        logger.info("STATUS DESCRIPTION : " + map.get("statusDesc"));
        
//        this.tm.testDBConnection();
        this.tm.selectSample();
        return mv;
    }
    
    @Override
    @RequestMapping(value = "/eStore", method = RequestMethod.GET)
    public ModelAndView showStore(){
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("msg", "Welcome suki!");
        return mv;
    }
    
    @RequestMapping(value = "/eStore/api", method = RequestMethod.GET)
    @ResponseBody
    public Store storeIndex(
            @RequestParam(value = "username", required = true) String usernameString, 
            @RequestParam(value = "itemname", required = true) String itemnameString){
        String methodName = "eStore/API";
        this.loggerInfo("START");
        this.loggerInfo("PARAMS START : " + "\nusername = " + usernameString + "\nitemname=" + itemnameString + " \nPARAMS END");
        Store store = new Store();
        ArrayList<HashMap> row = sm.getContent(usernameString, itemnameString);
        if(this.sm.errorEncountered() == true){
            //return model.errorDescription();
            store.setStatusCode("203");
            store.setStatusDesc("Error Accessing database!");
            this.loggerInfo(store.getStatusDesc());
            this.loggerInfo("END");
            return store;
        }
        if(row.isEmpty()){
            store.setStatusCode("203");
            store.setStatusDesc("Username or ItemName does not exist!");
            this.loggerInfo(store.getStatusDesc());
            this.loggerInfo("END");
            return store;
        }

        HashMap<String, String> map = row.get(0);
        double accBalance = Double.parseDouble(map.get("acc_balance"));
        double itemPrice = Double.parseDouble(map.get("item_price"));
        String itemId = map.get("item_id");
        String itemName = map.get("item_name");
        String accId = map.get("acc_id");
        String accUsername = map.get("acc_username");
        if(accBalance < itemPrice){
            store.setStatusCode("200");
            store.setStatusDesc("Insufficient Balance!");
            this.loggerInfo(store.getStatusDesc());
            this.loggerInfo("END");
            return store;
        }
        double newBalance = accBalance - itemPrice;
        if(this.sm.updateAccountBalance(accId, String.valueOf(newBalance))==0){
            store.setStatusCode("202");
            store.setStatusDesc("Balance Update Failed, Please try again!");
            this.loggerInfo(store.getStatusDesc());
            this.loggerInfo("END");
            return store;
        }
        
        if(this.sm.insertSoldItem(accId, itemId, String.valueOf(itemPrice))>0){
            store.setStatusCode("100");
            store.setStatusDesc("Success!");
            this.loggerInfo(store.getStatusDesc());
            this.loggerInfo("END");
            return  store;
        }
        store.setStatusCode("203");
        store.setStatusDesc("Insertion to sold item failed, Please contact system admin!");
        this.loggerInfo(store.getStatusDesc());
        this.loggerInfo("END");
        return store;
    }
}
