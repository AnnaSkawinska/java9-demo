package software.xsolve.java9demo;

import com.google.common.collect.ImmutableSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Java9DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Java9DemoApplication.class, args);
    System.out.println();
    System.out.println("Hello, Java 8 world!");
    System.out.println("Please, have some fruit: " + ImmutableSet.of("orange", "apple", "grapes"));
    System.out.println();
	}
}
