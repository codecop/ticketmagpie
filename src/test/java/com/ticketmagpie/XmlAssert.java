package com.ticketmagpie;

import static org.junit.Assert.assertThat;

import org.springframework.http.ResponseEntity;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.matchers.CompareMatcher;

class XmlAssert {

  public void bodyEquals(String expectedXml, ResponseEntity<String> response) {
    String actualXml = response.getBody();
    equals(expectedXml, actualXml);
  }

  public void equals(String expectedXml, String actualXml) {
    assertThat(actualXml, CompareMatcher.isSimilarTo(expectedXml). //
        withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText)). //
        ignoreWhitespace(). //
        ignoreComments());
  }

}
