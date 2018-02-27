module software.xsolve.java9demo {
  exports software.xsolve.java9demo;

  requires guava;
  requires spring.boot.autoconfigure;
  requires spring.boot;

  opens software.xsolve.java9demo to spring.core;
}
