package com.ticketmagpie.infrastructure.security;

import org.owasp.appsensor.core.AppSensorClient;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.DetectionPoint.Category;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.Event;
import org.owasp.appsensor.core.User;
import org.owasp.appsensor.core.event.EventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MonitoringPointTracker {

  @Autowired
  private AppSensorClient appSensorClient;

  @Autowired
  private EventManager ids;

  public void failedLoginFor(String username) {
    DetectionPoint detectionPoint = new DetectionPoint(Category.AUTHENTICATION, "AE3"); // high rate of attempts
    User user = new User(username);
    track(detectionPoint, user);
  }

  private void track(DetectionPoint detectionPoint, User user) {
    ids.addEvent(new Event(user, detectionPoint, getDetectionSystem()));
  }

  private DetectionSystem getDetectionSystem() {
    return new DetectionSystem(appSensorClient.getConfiguration().getServerConnection().getClientApplicationIdentificationHeaderValue());
  }

  public void badMethod() {
    DetectionPoint detectionPoint = new DetectionPoint(Category.REQUEST, "RE2");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = new User(username);
    
    track(detectionPoint, user);
  }

}
