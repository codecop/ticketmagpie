package com.ticketmagpie.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ticketmagpie.infrastructure.security.MonitoringPointTracker;

@Controller
@RequestMapping(method = { RequestMethod.POST })
public class TrackingController {

  @Autowired
  private MonitoringPointTracker tracker;

  @RequestMapping("/concert")
  public ResponseEntity<String> post() {
    // duplicate error handling to track issue
    tracker.badMethod();
    return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
  }

}
