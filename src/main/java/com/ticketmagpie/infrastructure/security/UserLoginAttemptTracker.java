package com.ticketmagpie.infrastructure.security;

import java.util.HashMap;
import java.util.Map;

import org.owasp.appsensor.core.AppSensorClient;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.DetectionPoint.Category;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.Event;
import org.owasp.appsensor.core.User;
import org.owasp.appsensor.core.event.EventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLoginAttemptTracker {

  private final Map<String, Integer> attemptsByUserName = new HashMap<>();

  @Autowired
  private AppSensorClient appSensorClient;

  @Autowired
  private EventManager ids;

  public int getAttemptsFor(String username) {
    return attemptsByUserName.getOrDefault(username, 0);
  }

  public void failedAttemptsFor(String username) {
    attemptsByUserName.put(username, getAttemptsFor(username) + 1);

    DetectionPoint detectionPoint = new DetectionPoint(Category.AUTHENTICATION, "AE3"); // high rate of attempts
    User user = new User(username);
    ids.addEvent(new Event(user, detectionPoint, getDetectionSystem()));
    System.out.println("FAILED LOGIN " + username + "!!!!");
  }

  public void successFor(String username) {
    attemptsByUserName.put(username, 0);
  }

  private DetectionSystem getDetectionSystem() {
    return new DetectionSystem(appSensorClient.getConfiguration().getServerConnection().getClientApplicationIdentificationHeaderValue());
  }

}
