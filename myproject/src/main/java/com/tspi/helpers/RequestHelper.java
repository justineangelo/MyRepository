/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.helpers;

import com.tspi.template.CoreTemplate;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONValue;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author JRANGEL
 */
public class RequestHelper extends CoreTemplate{
    
    public enum RequestMethod{
        POST, GET
    }
    public enum RequestProperty{
        REST, SOAP
    }
    private boolean errorOccured = false;
    private String errorDescription = "";
    private String rawRespString = "";
    
    
    private String strURI = "";
    private RequestMethod rt = RequestMethod.GET;//default request method
    private RequestProperty rp = RequestProperty.REST;//default request property
    private int requestTimeout = 10000;//default request timeout in miliseconds
    private int readTimeout = 5000;//default read timeout in mili seconds
    private HashMap<String, String> postData = null;
    /*
    Start Setter
    */
    public void setStringURI(String uri){
        this.strURI = uri;
    }
    public void setRequestMethod(RequestMethod rt){
        this.rt = rt;
    }
    public void setRequestProperty(RequestProperty rp){
        this.rp = rp;
    }
    public void setRequestConnnectionTimeout(int timeout){
        this.requestTimeout = timeout;
    }
    public void setRequestReadTimeout(int timeout){
        this.readTimeout = timeout;
    }
    public void setPostData(HashMap<String, String> postData){
        this.postData = postData;
    }
    /*
    End Setter
    */
    /*
    Start Getter
    */
    public String getErrorDesciption(){
        return this.errorDescription;
    }
    public boolean errorOccured(){
        return this.errorOccured;
    }
    public String getRawResponseString(){
        return this.rawRespString;
    }
    /*
    End Getter
    */
    
    /*
    Start Execution
    */
    public Object requestStart(){
        
        this.errorOccured = false;
        this.errorDescription = "";
        this.rawRespString = "";
        Object returnObject = null;
        HttpURLConnection connection = null;
        
        try {
            URL url = new URL(this.strURI);
            connection =  (HttpURLConnection)url.openConnection();
            if(this.rt == RequestMethod.GET){
                connection.setRequestMethod("GET");
            }else if(this.rt == RequestMethod.POST){
                connection.setRequestMethod("POST");
                if(!this.postData.isEmpty()){
                    boolean isFirstAppend = true;
                    StringBuilder sb = new StringBuilder();
                    for (HashMap.Entry entry : this.postData.entrySet()) {
                        if(!isFirstAppend){
                            sb.append("&");
                        }
                        sb.append(entry.getKey());
                        sb.append("=");
                        sb.append(entry.getValue());
                        isFirstAppend = false;
                    }
                    connection.setDoOutput(true);
                    try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                        wr.writeBytes(sb.toString());
                        wr.flush();
                        wr.close();
                    }catch(Exception e){
                        this.errorOccured = true;
                        this.errorDescription = "closing connection " + e.getMessage();
                    }
                }
            }
            if(this.rp == RequestProperty.REST){
                connection.setRequestProperty("Accept", "application/json");
            }else if(this.rp == RequestProperty.SOAP){
                connection.setRequestProperty("Accept", "appliction/xml");
            }
            
            connection.setConnectTimeout(this.requestTimeout);
            connection.setReadTimeout(this.readTimeout);
            connection.setAllowUserInteraction(false);
            connection.connect();
            
        } catch (Exception e) {
            this.errorOccured = true;
            this.errorDescription = e.getMessage();
        }finally{
            this.rawRespString = "has connection";
            try {
                if(connection!=null){
                    int status = connection.getResponseCode();
                    if(status == 200){
                        String outString;
                        try ( //success
                            InputStream inputStream = connection.getInputStream()) {
                            outString = this.streamReader(inputStream);
                            this.rawRespString = outString;
                            inputStream.close();
                            // Do something on outString
                        }
                        if(this.rp == RequestProperty.REST){
                            returnObject = JSONValue.parse(outString);
                        }else if (this.rp == RequestProperty.SOAP) {
    //                            returnObject = XML parse



                        }else{
                            returnObject = outString;
                        }

                    }
                    else{
                        //fail
                    }
                    connection.disconnect();
                }
            } catch (Exception e) {
                this.errorOccured = true;
                this.errorDescription = e.getMessage();
            }finally{
                /*parsing start*/

            }
                
            
        }
        return returnObject;
    }
    
    private String streamReader(InputStream stream) throws Exception{
        BufferedReader br = null;
        StringBuilder sb = null;
        String retString = "";
        try {
            br = new BufferedReader(new InputStreamReader(stream));
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            throw e;
        }finally{
            if(br!=null){
                br.close();
            }
            if(sb!=null){
                retString = sb.toString();
            } 
        }
       return retString;
    }
    /*
    End Execution
    */
}
