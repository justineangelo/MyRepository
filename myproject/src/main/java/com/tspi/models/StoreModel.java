/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.models;

import com.tspi.template.ModelTemplate;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author noc
 */
public class StoreModel extends ModelTemplate{
    public StoreModel() {
        //Constructor
        this.loadDatabase("store");
    }
    public ArrayList<HashMap> getContent(String usernameString, String itemNameString)
    {
        this.setDefaultQuery("SELECT account.id AS acc_id, account.username AS acc_username, account.balance AS acc_balance, item.id AS item_id, item.itemname AS item_name, item.price AS item_price \n" +
            "FROM \n" +
            "account, \n" +
            "item\n" +
            "WHERE\n" +
            "account.username='"+ usernameString +"' AND\n" +
            "item.itemname='"+ itemNameString +"'");
        this.startConnection();
        return this.outputResult();
    }
    
    public int updateAccountBalance(String acc_id, String newBalance)
    {
//        this.setDefaultQuery("UPDATE account SET balance = '" +  newBalance + "'\n" +
//            "WHERE id = '" + acc_id + "'");
//        this.startConnection();
        HashMap<String, String> update,where;
        update = new HashMap<>();
        where = (HashMap<String, String>)update.clone();
        update.put("balance", newBalance);
//        this.dbData(update);
        where.put("id", acc_id);
        this.dbWhere(where);
        this.dbUpdate("account", update);
        return this.updatedRows();
    }
    
    public int insertSoldItem(String acc_id, String item_id, String price)
    {
//        this.setDefaultQuery("INSERT INTO sold(account_id, item_id, price)\n" +
//            " VALUES ('" + acc_id +"','"+ item_id + "' , '" +  price + "');");
//        this.startConnection();
        this.loggerDebug("START ACTIVE RECORD : insert item Sold");
        HashMap<String, String> insertData = new HashMap<>();
        insertData.put("account_id", acc_id);
        insertData.put("item_id", item_id);
        insertData.put("price", price);
        this.dbData(insertData);
        this.dbInsert("sold");
        return this.updatedRows();
    }
}
