package com.ticketmagpie.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ticketmagpie.Concert;
import com.ticketmagpie.infrastructure.persistence.ConcertRepository;

@Controller
public class ApiController {

  @Autowired
  private ConcertRepository concertRepository;

  @RequestMapping(value = "/concerts.xml", method = GET, produces = { MediaType.APPLICATION_XML_VALUE })
  @ResponseBody
  public List<Concert> exportConcerts() {
    // TODO need to convert to DTOs so it only contains id, band, date, description
    return concertRepository.getAllConcerts();
  }

  // TODO concert[id].xml
  
  // TODO post concert, - how to do security?
  
}
