package com.ticketmagpie.infrastructure.security;

import javax.inject.Inject;
import javax.inject.Named;

import org.owasp.appsensor.core.User;
import org.owasp.appsensor.core.response.UserManager;
import org.springframework.context.annotation.Primary;

import com.ticketmagpie.infrastructure.persistence.UserRepository;

@Primary
@Named
public class MagpieUserManager implements UserManager {

  @Inject
  private UserRepository users;
  
  @Override
  public void logout(User user) {
  }

  @Override
  public void disable(User user) {
    // brutal
    users.delete(user.getUsername());
  }

}
