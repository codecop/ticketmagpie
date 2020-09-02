package com.ticketmagpie;

import java.util.Properties;

/**
 * Environment for integrated tests.
 */
public class TestEnvironment {

  private static final String HOST_PROPERTY_NAME = "magpie.server";
  private static final String PORT_PROPERTY_NAME = "magpie.port";

  public static boolean inEnvironment() {
    Properties properties = System.getProperties();
    return properties.containsKey(HOST_PROPERTY_NAME) && properties.containsKey(PORT_PROPERTY_NAME);
  }

  private static String host(String defaultHost) {
    if (inEnvironment()) {
      return System.getProperty(HOST_PROPERTY_NAME);
    } else {
      return defaultHost;
    }
  }

  private static int port(int defaultPort) {
    if (inEnvironment()) {
      return Integer.getInteger(PORT_PROPERTY_NAME);
    } else {
      return defaultPort;
    }
  }

  public static String server(String defaultHost, int defaultPort) {
    return "http://" + host(defaultHost) + ':' + port(defaultPort);
  }

  public static String baseUrl(String host, int port) {
    return server(host, port) + '/';
  }

}
