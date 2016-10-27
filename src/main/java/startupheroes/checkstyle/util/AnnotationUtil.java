package startupheroes.checkstyle.util;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static startupheroes.checkstyle.util.CommonUtil.getChildsByType;
import static startupheroes.checkstyle.util.CommonUtil.getSimpleAndFullNames;
import static startupheroes.checkstyle.util.CommonUtil.getSimpleName;

/**
 * @author ozlem.ulag
 */
public final class AnnotationUtil {

   private AnnotationUtil() {
   }

   /**
    * @param fullAnnotation : Full qualifier of annotation like java.lang.Object
    * @return : true if ast has any possible annotation, otherwise false
    */
   public static Boolean hasAnnotation(DetailAST ast, String fullAnnotation) {
      return getSimpleAndFullNames(fullAnnotation).stream()
                                                  .anyMatch(annotation -> AnnotationUtility.containsAnnotation(ast, annotation));
   }

   public static DetailAST getAnnotation(DetailAST ast, String fullAnnotation) {
      DetailAST simpleAnnotationAst = AnnotationUtility.getAnnotation(ast, getSimpleName(fullAnnotation));
      return nonNull(simpleAnnotationAst) ? simpleAnnotationAst : AnnotationUtility.getAnnotation(ast, fullAnnotation);
   }

   public static List<String> getKeys(DetailAST annotationAst) {
      List<DetailAST> keyValuePairAstList = getChildsByType(annotationAst, TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR);
      return keyValuePairAstList.stream()
                                .map(keyValuePairAst -> keyValuePairAst.getFirstChild().getText())
                                .collect(Collectors.toList());
   }

   /**
    * key -> ANNOTATION_MEMBER_VALUE_PAIR type ast
    */
   public static Map<String, DetailAST> getKeyValueAstMap(DetailAST annotationAst) {
      List<DetailAST> keyValuePairAstList = getChildsByType(annotationAst, TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR);
      return keyValuePairAstList.stream().collect(Collectors.toMap(keyValuePairAst -> keyValuePairAst.getFirstChild().getText(),
                                                                   Function.identity(),
                                                                   (v1, v2) -> null,
                                                                   LinkedHashMap::new));
   }

   /**
    * @param annotationKeyValueAst : ANNOTATION_MEMBER_VALUE_PAIR type ast
    * @return value of key
    */
   public static Optional<String> getValue(DetailAST annotationKeyValueAst) {
      Optional<String> result = Optional.empty();
      DetailAST exprAst = annotationKeyValueAst.findFirstToken(TokenTypes.EXPR);
      if (nonNull(exprAst)) {
         DetailAST literalValueAst = exprAst.getFirstChild();
         if (nonNull(literalValueAst)) {
            result = Optional.of(literalValueAst.getText());
         }
      }
      return result;
   }

   public static Object getDefaultValue(String fullAnnotationName, String key) {
      Object defaultValue = null;
      try {
         Class<?> annotationClass = Class.forName(fullAnnotationName);
         defaultValue = getDefaultValue(annotationClass, key);
      } catch (ClassNotFoundException ignored) {
      }
      return defaultValue;
   }

   public static Object getDefaultValue(Class<?> annotationClass, String key) {
      Object defaultValue = null;
      try {
         Method method = annotationClass.getMethod(key);
         defaultValue = method.getDefaultValue();
      } catch (NoSuchMethodException ignored) {
      }
      return defaultValue;
   }

   /**
    * key -> default value
    */
   public static Map<String, Object> getKeyDefaultValueMap(String fullAnnotationName, Collection<String> keys) {
      Map<String, Object> keyDefaultValueMap = new LinkedHashMap<>();
      try {
         Class<?> annotationClass = Class.forName(fullAnnotationName);
         keys.forEach(key -> keyDefaultValueMap.put(key, getDefaultValue(annotationClass, key)));
      } catch (ClassNotFoundException ignored) {
      }
      return keyDefaultValueMap;
   }

}