package com.ticketmagpie.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

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
    boolean done = false;
    if (user != null) {
      User userFromDatabase = userRepository.get(user);
      forgotPasswordService.userForgotPassword(userFromDatabase);
      done = true;
    }
    model.addAttribute("done", done);
    return "forgotpassword";
  }

  @RequestMapping("/concertimage")
  public void concertImage(@RequestParam(required = true) Integer id, HttpServletResponse httpServletResponse)
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
    try (InputStream inputStream = open(resourceName);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream()) {
      IOUtils.copy(inputStream, outputStream);
    }      
  }

  private String getResourceNameForConcertImage(String imageUrl) {
    return "static/images/" + imageUrl;
  }
  
  private InputStream open(String resourceName) throws FileNotFoundException, URISyntaxException {
    InputStream resourceStream = openInClasspath(resourceName);
    if (resourceStream != null) {
      return resourceStream;
    }
    
    InputStream fileStream = openInFileSystem(resourceName);
    if (fileStream != null) {
      return fileStream;
    }
    
    return open1x1BlankGif();
  }

  private InputStream openInClasspath(String resourceName) {
    return getClass().getClassLoader().getResourceAsStream(resourceName);
  }

  private FileInputStream openInFileSystem(String resourceName) throws URISyntaxException, FileNotFoundException {
    URL applicationProperties = getClass().getClassLoader().getResource("application.properties");
    if (!"file".equalsIgnoreCase(applicationProperties.getProtocol())) {
      return null;
    }

    File resources = new File(applicationProperties.toURI()).getParentFile();
    File resource = new File(resources, resourceName);
    if (resource.exists()) {
      return new FileInputStream(resource);
    }

    return null;
  }

  private InputStream open1x1BlankGif() {
    return new ByteArrayInputStream(new byte[] { //
        0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x1, 0, 0x1, 0, (byte) 0x80, 0, 0, (byte) 0xff, (byte) 0xff, (byte) 0xff, //
        0, 0, 0, 0x21, (byte) 0xf9, 0x4, 0, 0, 0, 0, 0, 0x2c, 0, 0, 0, 0, //
        0x1, 0, 0x1, 0, 0, 0x2, 0x2, 0x44, 0x1, 0, 0x3b //
    });
  }

}
