/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.template;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Core Singleton ENUM this is a singleton or design pattern to initialized once and upon compile time
 * @author JRANGEL
 */
enum CoreSingleton{
    /**
     * Access to singleton methods
     */
    INSTANCE;
    private final String loggerName = "Core Logger";
    private final Logger logger = Logger.getLogger(this.loggerName);
    private final HashMap<String, String> config = this._getConfig();
    
    //Do something with the initialization
    private CoreSingleton(){
        //initialization here
        this.myLogger().info("INITIALIZING CORE");
    }
    
    private HashMap<String, String> _getConfig(){
                HashMap<String, String> map = new HashMap<>();
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(CoreTemplate.class
                    .getClassLoader().getResource("config.properties").getPath()));
        } catch (Exception e) {
            this.myLogger().info("Error Getting config.properties desc : " + e.getMessage());
        }finally{
            Enumeration e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String propName = (String)e.nextElement();
                map.put(propName, prop.getProperty(propName));
            }
        }
        this.setLoggerMainThreshold(map);
        this.myLogger().info("LOADING CONFIGURATION");
        return map;
    }
    
    private void setLoggerMainThreshold(HashMap<String, String> config){
        boolean overrideBol = Boolean.valueOf(config.get("log.override"));
        for (Map.Entry property : config.entrySet()) {
            String key = property.getKey().toString();
            if(key.equalsIgnoreCase("log.threshold")&& overrideBol){
                String threshold = property.getValue().toString();
                this.myLogger().info("Overriding Current Logger!");
                this.myLogger().info("New LOG Threshold set to " + threshold);
                if (threshold.equalsIgnoreCase("info")) {
                    Logger.getLogger(loggerName).setLevel(Level.INFO);
                }else if(threshold.equalsIgnoreCase("debug")){
                    Logger.getLogger(loggerName).setLevel(Level.DEBUG);
                }else if(threshold.equalsIgnoreCase("error")){
                    Logger.getLogger(loggerName).setLevel(Level.ERROR);
                }else if(threshold.equalsIgnoreCase("off")){
                    Logger.getLogger(loggerName).setLevel(Level.OFF);
                }else{
                    Logger.getLogger(loggerName).setLevel(Level.ALL);
                }
            }
        }
    }
    //Getters
    /**
     * Get the current core logger
     * @return Logger
     */
    public Logger myLogger(){
        return this.logger;
    }
    /**
     * Get the configuration/Settings of the application
     * @return HashMap<String, String>
     */
    public HashMap<String, String> myConfig(){
        return this.config;
    }
}

/**
 * The main prototype template
 * @author JRANGEL
 */
public class CoreTemplate {
    /**
     * Used for logging info message
     * @param msg 
     */
    public final void loggerInfo(String msg){
        CoreSingleton.INSTANCE.myLogger().info(msg);
    }
    /**
     * Used for logging debug message
     * @param msg 
     */
    public final void loggerDebug(String msg){
        CoreSingleton.INSTANCE.myLogger().debug(msg);
    }
    /**
     * Used for logging error message
     * @param msg 
     */
    public final void loggerError(String msg){
        CoreSingleton.INSTANCE.myLogger().error(msg);
    }
    /**
     * Used for getting configuration set
     * @return HashMap 
     */
    public final HashMap<String, String> getConfig(){
         return CoreSingleton.INSTANCE.myConfig();
    }
}