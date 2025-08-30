package hexlet.code.config;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    private static Method bestHandlerFor(Class<?> exType) {
        var methods = Arrays.stream(ApiExceptionHandler.class.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(ExceptionHandler.class))
                .toList();

        Method best = null;
        var bestScore = Integer.MAX_VALUE;

        for (var m : methods) {
            var ann = m.getAnnotation(ExceptionHandler.class);
            Class<?> handled = null;

            for (var c : ann.value()) {
                if (c.isAssignableFrom(exType)) {
                    handled = c;
                    break;
                }
            }

            if (handled == null) {
                for (var p : m.getParameterTypes()) {
                    if (p.isAssignableFrom(exType)) {
                        handled = p;
                        break;
                    }
                }
            }

            if (handled == null) {
                continue;
            }

            var score = distance(exType, handled);
            if (score < bestScore) {
                bestScore = score;
                best = m;
            }
        }

        if (best == null) {
            throw new IllegalStateException("Handler not found for " + exType.getName());
        }
        return best;
    }

    private static int distance(Class<?> child, Class<?> parent) {
        var cur = child;
        var d = 0;
        while (cur != null && !parent.equals(cur)) {
            cur = cur.getSuperclass();
            d++;
        }
        return cur == null ? Integer.MAX_VALUE / 2 : d;
    }

    private static Object[] buildArgs(Method method, Exception ex) {
        var types = method.getParameterTypes();
        var args = new Object[types.length];

        for (var i = 0; i < types.length; i++) {
            var t = types[i];

            if (t.isInstance(ex)) {
                args[i] = ex;
            } else if (t.equals(WebRequest.class)) {
                args[i] = new ServletWebRequest(new MockHttpServletRequest());
            } else if (t.getName().endsWith(".HttpServletRequest")) {
                args[i] = new MockHttpServletRequest();
            } else if (t.getName().endsWith(".HttpServletResponse")) {
                args[i] = new MockHttpServletResponse();
            } else {
                args[i] = null;
            }
        }
        return args;
    }

    @Test
    void mapsValidationTo422() throws Exception {
        var handler = new ApiExceptionHandler();
        var method = bestHandlerFor(MethodArgumentNotValidException.class);

        BindingResult br = new BeanPropertyBindingResult(new Object(), "req");
        br.addError(new FieldError("req", "field", "must not be blank"));

        var p = new MethodParameter(
                ApiExceptionHandlerTest.class.getDeclaredMethod("stub", String.class),
                0
        );
        var ex = new MethodArgumentNotValidException(p, br);

        @SuppressWarnings("unchecked")
        var response = (ResponseEntity<Map<String, String>>) method.invoke(
                handler,
                buildArgs(method, ex)
        );

        assertThat(response.getStatusCode().value()).isEqualTo(422);
    }

    @SuppressWarnings("unused")
    private void stub(String v) {
    }
}
