package startupheroes.checkstyle.checks.custom;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import startupheroes.checkstyle.checks.BaseCheckTestSupport;

/**
 * @author ozlem.ulag
 */
public class EntityMandatoryVariableCheckTest extends BaseCheckTestSupport {

   @Test
   public void testByWrongInput() throws Exception {
      String[] expectedMessages = {"12: " + getCheckMessage("entityMandatoryVariableCheckMessage", "lastUpdatedAt")};
      test("TestWrongEntity.java", expectedMessages);
   }

   @Test
   public void testByCorrectInput() throws Exception {
      String[] expectedMessages = {};
      test("TestCorrectEntity.java", expectedMessages);
   }

   private void test(String fileName, String[] expectedMessages) throws Exception {
      verify(createCheckConfig(EntityMandatoryVariableCheck.class,
                               ImmutableMap.of("entityAnnotation", "javax.persistence.Entity",
                                               "mandatoryVariables", "createdAt, lastUpdatedAt")),
             getPath(fileName),
             expectedMessages);
   }

}
