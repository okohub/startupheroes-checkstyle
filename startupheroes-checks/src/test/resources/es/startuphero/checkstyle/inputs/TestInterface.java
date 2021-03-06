package es.startuphero.checkstyle.inputs;

public interface TestInterface {

  public static final String REDUNDANT_FIELD_PUBLIC = "Redundant public used!";

  static final String REDUNDANT_FIELD_STATIC = "Redundant static used!";

  final String REDUNDANT_FIELD_FINAL = "Redundant final used!";

  public Boolean redundantPublic();

  abstract Boolean redundantAbstract();

  void overrideMe();

  OtherInterface otherOperations() throws RuntimeException;

  interface OtherInterface {

    void sample();

    void sample2();
  }
}
