package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.nio.Relation;
import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.junit.jupiter.api.extension.*;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;
import java.lang.Class;
import java.lang.Object;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static org.echocat.unittest.utils.extensions.TemporaryPath.Utils.findTemporaryPathEnabledAnnotation;
import static org.echocat.unittest.utils.extensions.TemporaryPath.Utils.provide;
import static org.echocat.unittest.utils.extensions.TemporaryPaths.Scope.all;
import static org.echocat.unittest.utils.extensions.TemporaryPaths.Scope.each;
import static org.echocat.unittest.utils.nio.Relation.*;
import static org.echocat.unittest.utils.nio.TemporaryPathBroker.temporaryPathBrokerFor;
import static org.echocat.unittest.utils.utils.IOUtils.closeAll;

public class TemporaryPaths implements ParameterResolver, BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback {

    @Nonnull
    @GuardedBy("this")
    private final List<Resource> resources = new LinkedList<>();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();
        final Optional<Annotation> annotation = findTemporaryPathEnabledAnnotation(parameter);
        if (annotation.isPresent()) {
            final Class<?> parameterType = parameter.getType();
            if (!Path.class.equals(parameterType)) {
                //noinspection ThrowCaughtLocally
                throw new ParameterResolutionException("Parameter " + parameter + " is annotated with @" + annotation.get().annotationType().getSimpleName()
                    + " but is not of expected type " + Path.class.getName() + ".");
            }
            return true;
        }
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();
        final Annotation annotation = findTemporaryPathEnabledAnnotation(parameter)
            .orElseThrow(() -> new ParameterResolutionException("Expected to be annotated with one of " + TemporaryPath.class.getName() + " annotation."));
        try {
            boolean success = false;
            final Relation<?> relation = each.relationFor(extensionContext);
            final TemporaryPathBroker broker = temporaryPathBrokerFor(displayNameFor(extensionContext, each) + "." + parameterContext.getIndex());
            try {
                final Path path = provide(annotation, relation, broker);
                register(broker, each);
                success = true;
                return path;
            } finally {
                if (!success) {
                    broker.close();
                }
            }
        } catch (final ParameterResolutionException e) {
            throw e;
        } catch (final Exception e) {
            throw new ParameterResolutionException(e.getMessage(), e);
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        before(context.getRequiredTestClass(), all, context);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        before(context.getRequiredTestClass(), each, context);
    }

    protected void before(@Nonnull Class<?> type, @Nonnull Scope scope, @Nonnull ExtensionContext context) throws Exception {
        before(context, scope, type.getDeclaredFields());

        final Class<?> parent = type.getSuperclass();
        if (parent != null && !Objects.equals(parent, Object.class)) {
            before(parent, scope, context);
        }
    }

    protected void before(@Nonnull ExtensionContext context, @Nonnull Scope scope, @Nonnull Field... fields) throws Exception {
        for (final Field field : fields) {
            final int modifiers = field.getModifiers();
            if (!isAbstract(modifiers)) {
                if (scope == all && isStatic(modifiers)) {
                    before(context, scope, field);
                } else if (scope == each && !isStatic(modifiers)) {
                    before(context, scope, field);
                }
            }
        }
    }

    protected void before(@Nonnull ExtensionContext context, @Nonnull Scope scope, @Nonnull Field field) throws Exception {
        final Optional<Annotation> annotation = findTemporaryPathEnabledAnnotation(field);
        if (annotation.isPresent()) {
            boolean success = false;
            final Relation<?> relation = scope.relationFor(context);
            final TemporaryPathBroker broker = temporaryPathBrokerFor(displayNameFor(context, scope));
            try {
                final Path path = provide(annotation.get(), relation, broker);
                final Class<?> fieldType = field.getType();
                if (!Path.class.equals(fieldType)) {
                    throw new IllegalStateException("Field " + field + " is annotated with @" + annotation.get().annotationType().getSimpleName()
                        + " but is not of expected type " + Path.class.getName() + ".");
                }
                field.setAccessible(true);
                field.set(targetOf(relation), path);
                register(broker, scope);
                success = true;
            } finally {
                if (!success) {
                    broker.close();
                }
            }
        }
    }

    @Nonnull
    protected String displayNameFor(@Nonnull ExtensionContext context, @Nonnull Scope scope) {
        final StringBuilder sb = new StringBuilder();
        sb.append("junit#");
        sb.append(context.getRequiredTestClass().getName());
        if (scope == each) {
            final Method method = context.getRequiredTestMethod();
            sb.append('#').append(method.getName()).append('(');
            final Class<?>[] types = method.getParameterTypes();
            for (int i = 0; i < types.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(types[0].getName());
            }
            sb.append(')');
        }
        return sb.toString();
    }

    @Override
    public void afterEach(@Nonnull ExtensionContext context) throws Exception {
        after(each, context);
    }

    @Override
    public void afterAll(@Nonnull ExtensionContext context) throws Exception {
        after(all, context);
    }

    protected void after(@Nonnull Scope scope, @Nonnull ExtensionContext context) throws Exception {
        synchronized (this) {
            final List<Resource> toClose = resources
                .stream()
                .filter(candidate -> candidate.scope() == scope)
                .collect(toList());
            closeAll(toClose);
            resources.removeAll(toClose);
        }
    }

    @Nonnull
    public TemporaryPaths register(@Nonnull AutoCloseable closeable, @Nonnull Scope scope) {
        synchronized (this) {
            resources.add(new Resource(closeable, scope));
        }
        return this;
    }

    @Nonnull
    public List<Resource> resources() {
        synchronized (this) {
            return unmodifiableList(new ArrayList<>(resources));
        }
    }

    public static class Resource implements AutoCloseable {
        @Nonnull
        private final AutoCloseable delegate;
        @Nonnull
        private final Scope scope;

        protected Resource(@Nonnull AutoCloseable delegate, @Nonnull Scope scope) {
            this.delegate = delegate;
            this.scope = scope;
        }

        @Nonnull
        public AutoCloseable delegate() {
            return delegate;
        }

        @Override
        public void close() throws Exception {
            delegate.close();
        }

        @Nonnull
        public Scope scope() {
            return scope;
        }

    }

    public enum Scope {
        each(context -> objectRelationFor(context.getRequiredTestInstance())),
        all(context -> classRelationFor(context.getRequiredTestClass()));

        private final Function<ExtensionContext, Relation<?>> relation;

        Scope(Function<ExtensionContext, Relation<?>> relation) {this.relation = relation;}

        public Relation<?> relationFor(@Nonnull ExtensionContext context) {
            return relation.apply(context);
        }
    }
}
