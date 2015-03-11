/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.helpers;

/**
 *
 * @author noc
 */
public class Store {
    private String status_code;
    private String status_desc;
    
    public String getStatusCode()
    {
        return status_code;
    }
    public String getStatusDesc()
    {
        return status_desc;
    }
    
    public void setStatusCode(String status_code)
    {
        this.status_code = status_code;
    }
    public void setStatusDesc(String status_desc)
    {
        this.status_desc = status_desc;
    }
    
}
