package com.ticketmagpie.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RichErrorController extends BasicErrorController {

  @Autowired
  public RichErrorController(ErrorAttributes errorAttributes, ServerProperties properties) {
    super(errorAttributes, properties.getError());
  }

  @Override
  public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView model = super.errorHtml(request, response);

    Map<Object, Object> systemProperties = new HashMap<>();
    for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
      systemProperties.put(entry.getKey(), entry.getValue());
    }
    model.addObject("properties", systemProperties);

    model.addObject("env", System.getenv());

    return model;
  }

}