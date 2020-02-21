package com.ticketmagpie.infrastructure;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.stereotype.Component;

@Component
public class ImageService {

  public InputStream open(String resourceName) throws FileNotFoundException, URISyntaxException {
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
    File resources = resourcesRoot();
    File resource = new File(resources, resourceName);
    if (resource.exists()) {
      return new FileInputStream(resource);
    }

    return null;
  }

  private File resourcesRoot() throws URISyntaxException {
    URL applicationProperties = getClass().getClassLoader().getResource("application.properties");
    if ("file".equalsIgnoreCase(applicationProperties.getProtocol())) {
      return new File(applicationProperties.toURI()).getParentFile();
    }
    
    return new File(".");
  }

  private InputStream open1x1BlankGif() {
    return new ByteArrayInputStream(new byte[] { //
        0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x1, 0, 0x1, 0, (byte) 0x80, 0, 0, (byte) 0xff, (byte) 0xff, (byte) 0xff, //
        0, 0, 0, 0x21, (byte) 0xf9, 0x4, 0, 0, 0, 0, 0, 0x2c, 0, 0, 0, 0, //
        0x1, 0, 0x1, 0, 0, 0x2, 0x2, 0x44, 0x1, 0, 0x3b //
    });
  }

}
