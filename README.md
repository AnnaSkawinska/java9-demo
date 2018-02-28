# java9-demo
Demonstrational project for showing transition from Java8 Spring Boot project to modular Java 9 project.

Here's the steps required to convert a Java8 application to one using modules, runnable from within IntelliJ and with Gradle (as of 4.5)

1. Start with the branch "master". 
What's there is a simple project SpringBoot "Hello world" project using Guava, built with Gradle, runnable with Java8.
- make sure you're running Java < 9 (`$ java -version`)
- make sure you're running Gradle 4.5 (`$ gradle -version`)
- run the main class `Java9DemoApplication.java` from within IntelliJ,
- run the application with Gradle (`$ gradle run`)
- check what command exacly is being run (very first line in IntelliJ terminal on running `$ gradle run --debug`)
	 
2. Upgrade your Java version to 9
 - make sure it's done, running `$ java -version`

3. Start the application as it is, under Java 9
  - check what command is being called from IntelliJ / Gradle
  - the application is being started with all its dependencies on CLASSPATH, not MODULEPATH
  
4. Make the application a part of a module
  - raise the project's `sourceCompatibility` to `1.9` in `build.gradle`
  - add `module-info.java`, pick a name for the module (best practice: `software.xsolve.java9demo`)
  - observe how IntelliJ indicates compilation errors in `Java9DemoApplication`
  - make use of IntelliJ's code completion to add all required modules to `module-info`
  - run the app with Intellij (project compiles but expect a runtime error - "SQLException class not found")
  - reason: JDK's module containing `SQLException` is not found on modulepath
  - run the app with Gradle (expect build failure: "module not found")
  - reason: Gradle tries to compiles the project adding the dependencies to CLASSPATH instead of MODULEPATH. Currently Gradle's Java9 support is poor, we'll have to adjust the build scripts ourselves.

5. Add missing JDK modules to IntelliJ's run configuration
  - set VM Options to `--add-modules ALL-DEFAULT` in IntelliJ's run configuration
  - run the app from IntelliJ
  - expected error: our application's module not visible for Spring's Enhancer
  
6. Add the `exports` section to module definition
  - add `exports software.xsolve.java9demo;` to `module-info.java`
  - run the app from IntelliJ
  - expected error: Spring's unable to instantiate our app with reflection

7. Add `opens` section to module definition
  - add `opens software.xsolve.java9demo to spring.core;` to `module-info.java`
  - run the app from IntelliJ - SUCCESS!
  
8. To fix Gradle build process, adjust the build script
 - add the following to `build.gradle`:

```
ext.moduleName = 'software.xsolve.java9demo'
mainClassName = "$moduleName/software.xsolve.java9demo.Java9DemoApplication"

compileJava {
	inputs.property("moduleName", moduleName)
	doFirst {
		options.compilerArgs = [
				'--module-path', classpath.asPath,
		]
		classpath = files()
	}
}

run {
	inputs.property("moduleName", moduleName)
	doFirst {
		jvmArgs = [
				'--module-path', classpath.asPath,
				'--add-modules', 'ALL-DEFAULT',
				'--module', mainClassName
		]
		classpath = files()
	}
}
```
 - run app with Gradle (`$ gradle run --debug`)
 - the app is finally compiled and run using modulepath
 
 
