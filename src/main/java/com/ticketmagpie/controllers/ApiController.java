package com.ticketmagpie.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ticketmagpie.Concert;
import com.ticketmagpie.infrastructure.persistence.ConcertRepository;
import com.ticketmagpie.infrastructure.xml.ConcertResource;
import com.ticketmagpie.infrastructure.xml.ConcertsResource;

@Controller
@RequestMapping("/api")
public class ApiController {

  @Autowired
  private ConcertRepository concertRepository;

  @RequestMapping(value = "/concerts.xml", method = GET, produces = { MediaType.APPLICATION_XML_VALUE })
  // @RequestMapping(value = "/concerts.json", method = GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseBody
  public ConcertsResource exportConcerts() {
    List<Concert> concerts = concertRepository.getAllConcerts();
    return toResource(concerts);
  }

  private ConcertsResource toResource(List<Concert> concerts) {
    List<ConcertResource> resources = concerts. //
        stream(). //
        map(this::toResource). //
        collect(Collectors.toList());
    return new ConcertsResource(resources);
  }

  private ConcertResource toResource(Concert concert) {
    return new ConcertResource(concert.getId(), concert.getBand(), concert.getDate(), concert.getDescription());
  }

  // TODO concert[id].xml

  // TODO post concert, - how to do security?

}
