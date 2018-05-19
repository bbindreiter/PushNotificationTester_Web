package com.firstrowria.pushnotificationtester;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class HelloAppEngineTest {

  @Test
  public void test() throws IOException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    new ConnectServlet().doGet(null, response);
    Assert.assertEquals("text/plain", response.getContentType());
    Assert.assertEquals("1", response.getWriterContent().toString());
  }
}
