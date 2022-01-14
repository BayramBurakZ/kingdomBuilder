package kingdomBuilder.annotationProcessors;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Implements a template renderer.
 */
public class TemplateRenderer {

    /**
     * Capitalizes the first letter of given string.
     * @param name The string to capitalize the first letter of.
     * @return The string with its first letter capitalized.
     */
    private static String capitalizeFirstLetter(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Evaluates a method to retrieve the value of a placeholder.
     * @param name The name of the method to evaluate.
     * @param template The template object to which the method belongs to.
     * @return The evaluation result as a string.
     */
    private static <T> String evaluateFromContext(String name, T template) {
        String getterName;
        if(name.startsWith("get")) getterName = name;
        else getterName = "get" + capitalizeFirstLetter(name);

        Class<?> cls = template.getClass();
        Optional<Method> optMethod = Arrays
            .stream(cls.getMethods())
            .filter(m -> m.getParameterCount() == 0
                         && m.getReturnType() == String.class
                         && (m.getName().equals(name) || m.getName().equals(getterName)))
            .findFirst();

        Method method = optMethod.orElseThrow(
            () -> new RuntimeException("Failed to find source for " + name + " or " + getterName)
        );

        try { return (String) method.invoke(template); }
        catch(Exception e) { throw new RuntimeException(e); }
    }

    /**
     * Evaluates a placeholder and returns the value.
     * @param contents The template which this method operates on.
     * @param begin The beginning of the placeholder.
     * @param end The end of the placeholder.
     * @param template The context to the placeholder values from.
     */
    private static <T> String eval(String contents, int begin, int end, T template) {
        if(contents.startsWith("<%=", begin)) {
            final String name = contents
                    .substring(begin + 3, end - 1)
                    .stripLeading()
                    .stripTrailing();
            return evaluateFromContext(name, template);
        }

        return "";
    }

    /**
     * Renders a template, by substituting placeholders with results of the public methods provided by the object.
     * @param template The template object to render.
     * @return The rendered template as String.
     * @throws IOException when the template file could not be loaded.
     */
    public static <T> String render(T template) throws IOException {
        Class<?> cls = template.getClass();
        if(!cls.isAnnotationPresent(Template.class))
            throw new RuntimeException("Template objects must be annotated with @Template.");

        Template ta = cls.getAnnotation(Template.class);
        String resourcePath = !ta.resource().isEmpty()
                ? ta.resource()
                : cls.getSimpleName() + ".java.template";

        InputStream is  = cls.getResourceAsStream(resourcePath);
        if(is == null)
            throw new RuntimeException("Failed to fetch resource.");

        String contents = new String(is.readAllBytes());
        final StringBuilder sb = new StringBuilder();

        for(int it = 0; it < contents.length(); ++it) {
            if(contents.startsWith("<%", it)) {
                int end = contents.indexOf("%>", it);
                if(end == -1 || it + 2 == end)
                    throw new RuntimeException("Encountered EOF while scanning file.");

                sb.append(eval(contents, it, end, template));
                it = end + 1;
                continue;
            }

            sb.append(contents.charAt(it));
        }

        return sb.toString();
    }

}
