/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.template;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Model Template
 * Prototype template for ease in db transaction
 * @author JRANGEL
 */

public class ModelTemplate extends CoreTemplate{ 
    
    public enum DBType {
        MYSQL, POSTGRE, ORACLE;
    }
    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    private DBType dbType = DBType.MYSQL;
    
    private int updateRows = 0;//update result var
    private ArrayList<HashMap> finalResultSet = null;//select result var
    private boolean errorEncountered = false;//err encountered var

    private String dbPath = "";
    private String dbHost = "";
    private String dbPort = "";
    private String dbName = "";
    private String username = "";
    private String password = "";
    private String defaultQuery = "";
    private String errorString = "";
    private String dbWhere = "";
    private String dbSelect = "";
    private HashMap<String, String> dbData = null;
    private String dbLimit = "";
    /*
    Start Getter
    */
    public ArrayList<HashMap> outputResult()
    {
        this.loggerDebug("DB Output Result : " + this.finalResultSet.toString());
        return this.finalResultSet;
    }
    public int updatedRows()
    {
        this.loggerDebug("DB Updated Rows : " + this.updateRows);
        return this.updateRows;
    }
    public String errorDescription()
    {
        this.loggerDebug("DB Error String : " + this.errorString);
        return this.errorString;
    }
    public boolean errorEncountered()
    {
        this.loggerDebug("DB Error Boolean : " + String.valueOf(this.errorEncountered));
        return this.errorEncountered;
    }
    
    /*
    Active Record SETTER and GETTER
    TODO:
    */
    /**
     * Active record for database where
     * 
     * @param updateData
     */
    public void dbWhere(HashMap<String, String> updateData){
        StringBuilder sb = new StringBuilder(this.dbWhere);
        for (Map.Entry<String, String> entrySet : updateData.entrySet()) {
            if(this.dbWhere.length() > 0){
                sb.append(" AND ");
            }
            sb.append(entrySet.getKey());
            if(entrySet.getKey().contains("<")||entrySet.getKey().contains(">") || entrySet.getKey().contains("=")){
                //do nothing
            }else{
                sb.append("=");
            }
            sb.append("'");
            sb.append(entrySet.getValue());
            sb.append("'");
        }
        this.dbWhere = sb.toString();
    }
    /**
     * Active record for database select
     * 
     * @param dbSelect
     */
    public void dbSelect(String dbSelect){
        this.dbSelect = dbSelect;
    }
    /**
     * Active record for database dbLimit
     * 
     * @param limit
     */
    public  void dbLimit(int limit){
        StringBuilder sb = new StringBuilder();
        sb.append(" LIMIT ");
        sb.append(limit);
        this.dbLimit = sb.toString();
    }
    /**
     * Active record for database data for update or insert
     * 
     * @param dbData
     */
    public void dbData(HashMap<String, String> dbData){
        this.dbData = dbData;
    }
    /**
     * Active record for database data for dbGet
     * 
     * @param tableName
     * @return ArrayList
     */
    public ArrayList<HashMap> dbGet(String tableName){
        this.loggerDebug("ACTIVE RECORD DB GET START");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        if(this.dbSelect.length()>0){
            sb.append(this.dbSelect);
        }
        else{
            sb.append("*");
        }
        sb.append(" FROM ");
        sb.append(tableName);
        if(this.dbWhere.length() > 0){
            sb.append(" WHERE ");
            sb.append(this.dbWhere);
        }
        if(this.dbLimit.length() > 0){
            sb.append(this.dbLimit);
        }
        sb.append(";");
        this.defaultQuery = sb.toString();
        this.startConnection();
        return this.outputResult();
    }
    /**
     * Active record for database update, supply tableName to perform
     * database insert.
     * 
     * @param tableName
     * @return boolean
     */
    public int dbUpdate(String tableName){
        this.loggerDebug("ACTIVE RECORD DB UPDATE START");
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(tableName);
        sb.append(" SET ");
        boolean isFirstVal = true;
        for (Map.Entry<String, String> entrySet : this.dbData.entrySet()) {
            if(!isFirstVal){
                sb.append(",");

            }
            sb.append(entrySet.getKey());
            sb.append("=");
            sb.append("'");
            sb.append(entrySet.getValue());
            sb.append("'");
            isFirstVal = false;
        }
        sb.append(" WHERE ");
        sb.append(this.dbWhere);
        sb.append(";");
        this.defaultQuery = sb.toString();
        this.startConnection();
        this.loggerDebug("Updated ROWS : " + this.updatedRows());
        return this.updatedRows();
    }
    /**
     * Active record for database update, supply tableName and updateData to perform
     * database insert.
     * 
     * @param tableName
     * @param updateData
     * @return boolean
     */
    public int dbUpdate(String tableName, HashMap<String, String> updateData){
        this.loggerDebug("ACTIVE RECORD DB UPDATE START");
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(tableName);
        sb.append(" SET ");
        this.dbData = updateData;
        boolean isFirstVal = true;
        for (Map.Entry<String, String> entrySet : this.dbData.entrySet()) {
            if(!isFirstVal){
                sb.append(",");

            }
            sb.append(entrySet.getKey());
            sb.append("=");
            sb.append("'");
            sb.append(entrySet.getValue());
            sb.append("'");
            isFirstVal = false;
        }
        sb.append(" WHERE ");
        sb.append(this.dbWhere);
        sb.append(";");
        this.defaultQuery = sb.toString();
        this.startConnection();
        this.loggerDebug("Updated ROWS : " + this.updatedRows());
        return this.updatedRows();
    }
    /**
     * Active record for database insert, supply tableName to perform
     * database insert.
     * 
     * @param tableName
     * @return boolean
     */
    public boolean dbInsert(String tableName){
        this.loggerDebug("ACTIVE RECORD DB INSERT START");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(tableName);
        boolean isFirstVal = true;
        StringBuilder sbCol = new StringBuilder();
        StringBuilder sbVal = new StringBuilder();
        for (Map.Entry<String, String> entrySet : this.dbData.entrySet()) {
            if(!isFirstVal){
                sbCol.append(",");
                sbVal.append(",");
            }
            sbCol.append(entrySet.getKey());
            sbVal.append(entrySet.getValue());
            isFirstVal = false;
        }
        sb.append(" ( ");
        sb.append(sbCol.toString());
        sb.append(" ) VALUES ( ");
        sb.append(sbVal.toString());
        sb.append(" );");
        this.defaultQuery = sb.toString();
        this.startConnection();
        this.loggerDebug("Inserted ROWS : " + this.updatedRows());
        return !this.errorEncountered();
    }
    /**
     * Active record for database insert, supply tableName and insertData to perform
     * database insert.
     * 
     * @param tableName
     * @param insertData
     * @return boolean
     */
    public boolean dbInsert(String tableName, HashMap<String, String> insertData){
        this.loggerDebug("ACTIVE RECORD DB INSERT START");
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(tableName);
        this.dbData = insertData;
        boolean isFirstVal = true;
        StringBuilder sbCol = new StringBuilder();
        StringBuilder sbVal = new StringBuilder();
        for (Map.Entry<String, String> entrySet : this.dbData.entrySet()) {
            if(!isFirstVal){
                sbCol.append(",");
                sbVal.append(",");
            }
            sbCol.append(entrySet.getKey());
            sbVal.append(entrySet.getValue());
            isFirstVal = false;
        }
        sb.append(" ( ");
        sb.append(sbCol.toString());
        sb.append(" ) VALUES ( ");
        sb.append(sbVal.toString());
        sb.append(" );");
        this.defaultQuery = sb.toString();
        this.startConnection();
        return !this.errorEncountered();
    }
     /**
     * Active record for database delete, supply tableName to perform
     * database insert.
     * 
     * @param tableName
     * @return boolean
     */
    public boolean dbDelete(String tableName){
        this.loggerDebug("ACTIVE RECORD DB DELETE START");
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(tableName);
        if(this.dbWhere.length() > 0){
            sb.append(" WHERE ");
            sb.append(this.dbWhere);
        }
        sb.append(";");
        this.defaultQuery = sb.toString();
        this.startConnection();
        this.loggerDebug("Inserted ROWS : " + this.updatedRows());
        return !this.errorEncountered();
    }
    /**
     * load Database for multiple database access
     * 
     * @param dbName
     * @param bol
     * @return ModelTemplate
     */
    protected ModelTemplate loadDatabase(String dbName, boolean bol){
        ModelTemplate model = new ModelTemplate();
        this.loggerDebug("START LOAD DATABASE");
        HashMap<String, String> config = this.getConfig();
        StringBuilder sb = new StringBuilder();
        sb.append("db.");
        sb.append(dbName);
        sb.append(".");
        this.loggerDebug("LOAD DATABASE FROM CONFIG : " + dbName);
        String baseConfigName = sb.toString();
        model.setDefaultDBPort(config.get(baseConfigName + "port"));
        model.setDefaultHost(config.get(baseConfigName + "host"));
        model.setDefaultDBName(config.get(baseConfigName + "dbname"));
        model.setDefaultUsername(config.get(baseConfigName + "username"));
        model.setDefaultPassword(config.get(baseConfigName + "password"));
        if(config.get(baseConfigName + "dbtype").equalsIgnoreCase("postgre")){
            model.setDBType(DBType.POSTGRE);
        }else if(config.get(baseConfigName + "dbtype").equalsIgnoreCase("mysql")){
            model.setDBType(DBType.MYSQL);
        }else if(config.get(baseConfigName + "dbtype").equalsIgnoreCase("oracle")){
            model.setDBType(DBType.ORACLE);
        }else{
            model.setDBType(DBType.MYSQL);
        } 
        try {
            model.createDBPath();
        } catch (Exception e) { 
            this.loggerInfo("Error occured : " + e.getMessage());
        }
        this.loggerDebug("END LOAD DATABASE");
        /**
         * return the instantiated model
         */
        return model;
    }
    /*
    End Getter
    */
    /**
     * @description : for active record 
     */
    private void clean(){
        this.loggerDebug("START parameters clean");
        this.dbWhere = "";
        this.dbSelect = "";
        this.defaultQuery = "";
        this.dbData = null;
        this.loggerDebug("END parameters clean");
    }
    /*
    Start Setter
    */
    /**
     * load Database for single database access
     * 
     * @param dbName 
     */
    protected void loadDatabase(String dbName){
        this.loggerDebug("START LOAD DATABASE");
        HashMap<String, String> config = this.getConfig();
        StringBuilder sb = new StringBuilder();
        sb.append("db.");
        sb.append(dbName);
        sb.append(".");
        this.loggerDebug("LOAD DATABASE FROM CONFIG : " + dbName);
        String baseConfigName = sb.toString();
        this.setDefaultDBPort(config.get(baseConfigName + "port"));
        this.setDefaultHost(config.get(baseConfigName + "host"));
        this.setDefaultDBName(config.get(baseConfigName + "dbname"));
        this.setDefaultUsername(config.get(baseConfigName + "username"));
        this.setDefaultPassword(config.get(baseConfigName + "password"));
        if(config.get(baseConfigName + "dbtype").equalsIgnoreCase("postgre")){
            this.setDBType(DBType.POSTGRE);
        }else if(config.get(baseConfigName + "dbtype").equalsIgnoreCase("mysql")){
            this.setDBType(DBType.MYSQL);
        }else if(config.get(baseConfigName + "dbtype").equalsIgnoreCase("oracle")){
            this.setDBType(DBType.ORACLE);
        }else{
            this.setDBType(DBType.MYSQL);
        } 
        try {
            this.createDBPath();
        } catch (Exception e) { 
            this.loggerInfo("Error occured : " + e.getMessage());
        }
        this.loggerDebug("END LOAD DATABASE");
    }
    public void setDefaultUsername(String username){
        this.loggerDebug("Set Default DB Username : " + username);
        this.username = username;
    }
    public  void setDefaultPassword(String password){
        this.loggerDebug("Set Default DB Password : " + password);
        this.password = password;
    }
    public void setDefaultHost(String dbHost){
        this.loggerDebug("Set Default DB Host : " + dbHost);
        this.dbHost = dbHost;
    }
    public void setDefaultDBPort(String dbPort){
        this.loggerDebug("Set Default DB Port : " + dbPort);
        this.dbPort = dbPort;
    }
    public void setDefaultDBName(String dbName){
        this.loggerDebug("Set Default DB Name : " + dbName);
        this.dbName = dbName;
    }
    public void setDBType(DBType dbType){
        this.loggerDebug("Set DB Type : " + dbType.toString());
        this.dbType = dbType;
    }
    public void setDefaultQuery(String defaultQuery)
    {
        this.loggerDebug("SETTING DEFAULT QUERY");
        this.defaultQuery = defaultQuery;
    }
    /*
    End Setter
    */
    
    
    private void createDBPath() throws Exception
    {
        String path = "";
        if(this.dbHost.length() == 0){
            throw new Exception("Database Host Missing!");
        }else if(this.dbName.length()==0){
            throw new Exception("Database Name Missing!");
        }
        path += this.dbHost;
        if(this.dbPort.length()>0){
            path += ":" + this.dbPort;
        }
        path += "/" + this.dbName;
        switch (this.dbType){
            case MYSQL:
                this.dbPath = "jdbc:mysql://" + path;
                break;
            case POSTGRE:
                this.dbPath = "jdbc:postgresql://" + path;
                break;
            default:
                this.dbPath = "jdbc:mysql://" + path;
                break;
        }
    }

    public void startConnection()
    {
        this.loggerDebug("START DB Transaction");
        this.finalResultSet = new ArrayList<>();
        this.errorEncountered = false;
        this.errorString = "";
        this.rs = null;
        this.updateRows = 0;
        if(this.dbPath.length()==0){
            try {
                this.createDBPath();
            } catch (Exception e) {
                this.errorString = e.getMessage();
                this.errorEncountered = true;
                this.loggerError("PATH CREATION EXEMPTION " + e.getMessage());
            }
        }
        try 
        {
            if(this.defaultQuery.trim().length()< 6){
                this.errorString = "Query is Missing!";
                this.errorEncountered = true;
                this.loggerError("DB Transaction ERROR : " + this.errorString);
                throw new Exception(this.errorString);
            }
            try {
                if (this.dbType == DBType.MYSQL) {
                    Class.forName("com.mysql.jdbc.Driver");
                }else if(this.dbType == DBType.POSTGRE){
                    Class.forName("org.postgresql.Driver");
                }else{
                    Class.forName("com.mysql.jdbc.Driver");
                }
            } catch (Exception e) {
                this.errorString = e!=null?e.getMessage():"Fail to load DB Driver";
                this.errorEncountered = true;
                this.loggerError("DB Transaction ERROR : " + this.errorString);
            }
            this.con = DriverManager.getConnection(this.dbPath, this.username, this.password);
            this.loggerDebug("DB Connection OPEN");
            this.st = con.createStatement();
            this.loggerDebug("DB Transaction Query : " + this.defaultQuery);
            String subString = this.defaultQuery.toLowerCase().trim().substring(0, 7);
            if(subString.contains("update".toLowerCase()) || subString.contains("insert".toLowerCase())){
                this.updateRows = this.st.executeUpdate(this.defaultQuery);
            }
            else{
                this.rs = this.st.executeQuery(this.defaultQuery);
            }
            this.loggerDebug("Execute Performed");
            if(subString.contains("update".toLowerCase())){
                this.loggerDebug("DB Transaction Type : UPDATE");
            }else if(subString.contains("insert".toLowerCase())){
                this.loggerDebug("DB Transaction Type : INSERT");
            }else if(subString.contains("select".toLowerCase())){
                this.loggerDebug("DB Transaction Type : SELECT");
                try {
                    ResultSetMetaData rsMeta = this.rs.getMetaData();
                    int columnCount = rsMeta.getColumnCount();
                    HashMap<String, String> row = new HashMap<>();
                    while (this.rs.next()) {
                        for (int i = 1; i <= columnCount; i++)
                        {
                           row.put(rsMeta.getColumnLabel(i), this.rs.getString(i));
                        }
                        this.finalResultSet.add((HashMap)row.clone());
                        row.clear();
                    }
                } catch (Exception e) {
                    this.loggerError("DB output ERROR via Result set conversion : " + e.getMessage());
                }
            }else if(subString.contains("delete".toLowerCase())){
                this.loggerDebug("DB Transaction Type : DELETE");
            }  
        } catch (Exception e) 
        {
            this.errorEncountered = true;
            this.errorString = e.getMessage();
            this.loggerError("DB Transaction Error : " + this.errorString + e.toString());
        }
        finally
        {
            
            try {
                if (this.rs != null) {
                    this.rs.close();
                }
                if (this.st != null) {
                    this.st.close();
                }
                if (this.con != null) {
                    this.con.close();
                }
                this.clean();
                this.loggerDebug("DB Connection CLOSE");
                this.loggerDebug("END DB Transaction");
            } catch (Exception e) {
                
                this.errorEncountered = true;
                this.errorString = e.getMessage();
                this.loggerError("DB Transaction ERROR : " + this.errorString);
            }
        }
    }
}