package com.ticketmagpie.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ticketmagpie.Concert;
import com.ticketmagpie.infrastructure.persistence.ConcertRepository;
import com.ticketmagpie.infrastructure.persistence.LastIdInserted;
import com.ticketmagpie.infrastructure.xml.ConcertResource;
import com.ticketmagpie.infrastructure.xml.ConcertsResource;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/api")
public class ApiController {

  @Autowired
  private ConcertRepository concertRepository;
  @Autowired
  private LastIdInserted lastIdInserted;

  @RequestMapping(value = "/concerts.xml", method = GET, produces = { MediaType.APPLICATION_XML_VALUE })
  @ResponseBody
  @ApiOperation(value = "Returns all concerts.", response = ConcertsResource.class)
  public ConcertsResource exportConcerts() {
    List<Concert> concerts = concertRepository.getAllConcerts();
    return toResource(concerts);
  }

  @RequestMapping(value = "/concerts/{concertId}.xml", method = GET, produces = { MediaType.APPLICATION_XML_VALUE })
  @ApiOperation(value = "Returns a single concert.", response = ConcertResource.class)
  public ResponseEntity<?> exportConcert(@PathVariable("concertId") Integer concertId) {
    try {

      Concert concert = concertRepository.get(concertId);
      return ResponseEntity.ok(toResource(concert));

    } catch (EmptyResultDataAccessException ignored) {
      return ResponseEntity.notFound().build();
    }
  }

  private ConcertsResource toResource(List<Concert> concerts) {
    List<ConcertResource> resources = concerts. //
        stream(). //
        map(this::toResource). //
        collect(Collectors.toList());
    return new ConcertsResource(resources);
  }

  private ConcertResource toResource(Concert concert) {
    return toResource(concert, concert.getId());
  }

  private ConcertResource toResource(Concert concert, Integer id) {
    return new ConcertResource(id, concert.getBand(), concert.getDate(), concert.getDescription());
  }

  @RequestMapping(value = "/concerts.xml", method = POST, consumes = { MediaType.APPLICATION_XML_VALUE }, produces = {
      MediaType.APPLICATION_XML_VALUE })
  @ApiOperation(value = "Creates a new concert.", response = ConcertResource.class)
  public ResponseEntity<ConcertResource> addConcert(@RequestBody ConcertResource requestResource) throws URISyntaxException {
    Concert concert = new Concert(null, requestResource.getBand(), requestResource.getDate(), requestResource.getDescription(), null,
        new byte[0]);

    concertRepository.save(concert);
    int newId = lastIdInserted.get();

    ConcertResource responseResource = toResource(concert, newId);
    return ResponseEntity.created(new URI("/api/concerts/" + newId + ".xml")).body(responseResource);
  }

}
