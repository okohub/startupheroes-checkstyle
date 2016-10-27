package startupheroes.checkstyle.checks.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.nonNull;
import static startupheroes.checkstyle.util.AnnotationUtil.getKeyDefaultValueMap;
import static startupheroes.checkstyle.util.AnnotationUtil.getKeyValueAstMap;
import static startupheroes.checkstyle.util.AnnotationUtil.getValue;
import static startupheroes.checkstyle.util.ClassUtil.getImportSimpleFullNameMap;
import static startupheroes.checkstyle.util.CommonUtil.getFullName;
import static startupheroes.checkstyle.util.CommonUtil.getSimpleName;

/**
 * @author ozlem.ulag
 */
public class RedundantDefaultAnnotationParameterAssignCheck extends AbstractCheck {

   /**
    * A key is pointing to the warning message text in "messages.properties" file.
    */
   private static final String MSG_KEY = "redundantDefaultAnnotationParameterAssignCheckMessage";

   private Map<String, String> importSimpleFullNameMap;

   @Override
   public int[] getDefaultTokens() {
      return getAcceptableTokens();
   }

   @Override
   public int[] getAcceptableTokens() {
      return new int[]{TokenTypes.ANNOTATION};
   }

   @Override
   public int[] getRequiredTokens() {
      return getAcceptableTokens();
   }

   @Override
   public void beginTree(DetailAST rootAST) {
      importSimpleFullNameMap = getImportSimpleFullNameMap(rootAST);
   }

   @Override
   public void visitToken(final DetailAST ast) {
      Map<String, DetailAST> annotationKeyValueAstMap = getKeyValueAstMap(ast);
      if (!annotationKeyValueAstMap.isEmpty()) {
         String annotationSimpleName = getSimpleName(ast);
         String fullAnnotationName = importSimpleFullNameMap.containsKey(annotationSimpleName) ?
             importSimpleFullNameMap.get(annotationSimpleName) : getFullName(ast);
         Set<String> keys = annotationKeyValueAstMap.keySet();
         Map<String, Object> keyDefaultValueMap = getKeyDefaultValueMap(fullAnnotationName, keys);
         for (String key : keys) {
            Optional<String> value = getValue(annotationKeyValueAstMap.get(key));
            if (value.isPresent()) {
               Object defaultValue = keyDefaultValueMap.get(key);
               if (nonNull(defaultValue) && value.get().equals(defaultValue.toString())) {
                  log(ast.getLineNo(), MSG_KEY, key);
               }
            }
         }
      }
   }

}
