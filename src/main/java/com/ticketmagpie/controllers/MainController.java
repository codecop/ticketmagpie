package com.ticketmagpie.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ticketmagpie.Concert;
import com.ticketmagpie.Ticket;
import com.ticketmagpie.User;
import com.ticketmagpie.infrastructure.ImageService;
import com.ticketmagpie.infrastructure.persistence.CommentRepository;
import com.ticketmagpie.infrastructure.persistence.ConcertRepository;
import com.ticketmagpie.infrastructure.persistence.TicketRepository;
import com.ticketmagpie.infrastructure.persistence.UserRepository;
import com.ticketmagpie.infrastructure.security.ForgotPasswordService;

@Controller
public class MainController {

  @Autowired
  private ConcertRepository concertRepository;

  @Autowired
  private TicketRepository ticketRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private ForgotPasswordService forgotPasswordService;

  @Autowired
  private ImageService imageService;

  @RequestMapping("/")
  public String index(Model model) {
    model.addAttribute("concerts", concertRepository.getAllConcerts());
    return "index";
  }

  @RequestMapping("/login")
  public String login() {
    return "login";
  }

  @RequestMapping("/registration")
  public String registration
      (@RequestParam(required = false, name = "username") String username, 
          @RequestParam(required = false, name = "password") String password, 
          @RequestParam(required = false, name = "email") String email, 
          @RequestParam(required = false, name = "role", defaultValue = "USER") String role) {
    if (username == null) {
      return "registration";
    }
    userRepository.save(new User(username, password, email, role));
    return "login";
  }

  @RequestMapping("/ticket")
  public String ticket(@RequestParam Integer id, Model model) {
    Ticket ticket = ticketRepository.get(id);
    model.addAttribute("ticket", ticket);
    return "ticket";
  }

  @RequestMapping("/forgotpassword")
  public String forgotPassword(@RequestParam(required = false) String user, Model model) {
    boolean error = false;
    boolean done = false;
    if (user != null) {
      User userFromDatabase = userRepository.get(user);
      if (userFromDatabase != null) {
        forgotPasswordService.userForgotPassword(userFromDatabase);
        done = true;
      } else {
        model.addAttribute("username", user);
        error = true;
      }
    }
    model.addAttribute("error", error);
    model.addAttribute("done", done);
    return "forgotpassword";
  }

  @RequestMapping("/concertimage")
  public void concertImage(@RequestParam Integer id, HttpServletResponse httpServletResponse)
      throws IOException, URISyntaxException {
    Concert concert = concertRepository.get(id);
    if (concert.getImageUrl() != null && !concert.getImageUrl().isEmpty()) {
      concertImageFromUrl(httpServletResponse, concert.getImageUrl());
    } else {
      concertImageFromBlob(httpServletResponse, concert.getImageBlob());
    }
  }

  @RequestMapping("/redirect")
  public void redirect(@RequestParam String url, HttpServletResponse httpServletResponse)
      throws IOException {
    httpServletResponse.sendRedirect(url);
  }

  @RequestMapping("/concert")
  public String concert(@RequestParam Integer id, Model model) {
    model.addAttribute("concert", concertRepository.get(id));
    model.addAttribute("comments", commentRepository.getAllForConcert(id));
    return "concert";
  }

  private void concertImageFromBlob(HttpServletResponse httpServletResponse, byte[] imageBlob)
      throws IOException {
    try (ServletOutputStream outputStream = httpServletResponse.getOutputStream()) {
      outputStream.write(imageBlob);
    }
  }

  private void concertImageFromUrl(HttpServletResponse httpServletResponse, String imageUrl)
      throws IOException, URISyntaxException {
    
    if (imageUrl.startsWith("http")) {
        httpServletResponse.sendRedirect(imageUrl);
        return;
    }
    
    String resourceName = getResourceNameForConcertImage(imageUrl);
    try (InputStream inputStream = imageService.open(resourceName);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream()) {
      IOUtils.copy(inputStream, outputStream);
    }      
  }

  private String getResourceNameForConcertImage(String imageUrl) {
    return "static/images/" + imageUrl;
  }
  
}
