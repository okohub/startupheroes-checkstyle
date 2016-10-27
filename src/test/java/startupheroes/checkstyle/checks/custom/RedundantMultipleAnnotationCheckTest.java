package startupheroes.checkstyle.checks.custom;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

/**
 * @author ozlem.ulag
 */
public class RedundantMultipleAnnotationCheckTest extends BaseCheckTestSupport {

   private static final String MSG_KEY = "redundantMultipleAnnotationCheckMessage";

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"25: " + getCheckMessage(MSG_KEY, "Id", "Column"),
                                   "33: " + getCheckMessage(MSG_KEY, "Id", "Column")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(RedundantMultipleAnnotationCheck.class,
                               ImmutableMap.of("tokens", "VARIABLE_DEF",
                                               "redundantAnnotationPairs", "javax.persistence.Id:javax.persistence.Column")),
             getPath(fileName),
             expectedMessages);
   }

}
