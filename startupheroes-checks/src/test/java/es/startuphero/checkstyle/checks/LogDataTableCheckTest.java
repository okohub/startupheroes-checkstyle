package es.startuphero.checkstyle.checks;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * @author ozlem.ulag
 */
public class LogDataTableCheckTest extends BaseCheckTestSupport {

  private static final String MSG_KEY = "keep.log.data.table";

  @Test
  public void testLogEntity() throws Exception {
    String[] expectedMessages = {"31: " + getCheckMessage(MSG_KEY, "request", 255),
        "34: " + getCheckMessage(MSG_KEY, "response", 255),
        "43: " + getCheckMessage(MSG_KEY, "uri", 255)
    };
    test("SmsProviderLog.java", expectedMessages);
  }

  @Test
  public void testByAbstractEntity() throws Exception {
    String[] expectedMessages = {};
    test("AbstractUserListItem.java", expectedMessages);
  }

  private void test(String fileName, String[] expectedMessages) throws Exception {
    verify(createCheckConfig(LogDataTableCheck.class,
        ImmutableMap.of("typeAnnotation", "javax.persistence.Entity",
            "abstractTypeAnnotation", "javax.persistence.MappedSuperclass",
            "columnAnnotation", "javax.persistence.Column",
            "limitLength", "255")),
        getPath(fileName),
        expectedMessages);
  }
}