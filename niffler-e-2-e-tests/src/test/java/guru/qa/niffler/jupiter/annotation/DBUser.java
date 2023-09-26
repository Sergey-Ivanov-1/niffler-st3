package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.DBUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBUser {

	String username() default "";

	String password() default "";
}
