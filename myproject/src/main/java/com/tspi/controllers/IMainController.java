/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.controllers;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author noc
 */
public interface IMainController {
    public String returnJsonString();
    public ModelAndView showParsedPage();
    public ModelAndView showStore();
}
