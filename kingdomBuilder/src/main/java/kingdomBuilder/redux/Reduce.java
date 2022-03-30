package kingdomBuilder.redux;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation used to mark methods as reducers.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Reduce {

    /**
     * {@return the action which this method reduces on.}
     */
    String action();
}
