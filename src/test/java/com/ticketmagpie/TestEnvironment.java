package com.ticketmagpie;

/**
 * Environment for integrated tests.
 */
public class TestEnvironment {

  private static final String HOST_PROPERTY_NAME = "magpie.host";
  private static final String PORT_PROPERTY_NAME = "magpie.port";

  public static String host(String host, int port) {
    return "http://" + host + ':' + port;
  }

  public static String baseUrl(String host, int port) {
    return host(host, port) + '/';
  }

//  host = System.getProperty("");
//  port = Integer.getInteger("magpie.port");

}
