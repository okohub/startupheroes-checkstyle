package es.startuphero.checkstyle.checks;

import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class MissingEmptyLineCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "missing.empty.line";

  @Test
  public void testByWrongEntity() throws Exception {
    String[] expectedMessages = {"188: " + getCheckMessage(MSG_KEY)};
    test("TestWrongEntity.java", expectedMessages);
  }

  @Test
  public void testByWrongInterface() throws Exception {
    String[] expectedMessages = {"23: " + getCheckMessage(MSG_KEY),
        "24: " + getCheckMessage(MSG_KEY)};
    test("TestInterface.java", expectedMessages);
  }

  @Test
  public void testByCorrectInput() throws Exception {
    String[] expectedMessages = {};
    test("TestCorrectEntity.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(MissingEmptyLineCheck.class),
        getPath(fileName),
        expectedMessages);
  }
}