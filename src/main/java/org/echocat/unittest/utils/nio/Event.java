package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public interface Event {

    @Nonnull
    EventType type();

    @Nonnull
    List<Object> arguments();

    @Nonnull
    static Event eventOf(@Nonnull EventType type, @Nonnull Object... arguments) {
        return new Default(type, asList(arguments));
    }

    @Immutable
    class Default implements Event {

        @Nonnull
        private final EventType type;
        @Nonnull
        private final List<Object> arguments;

        protected Default(@Nonnull EventType type, @Nonnull Collection<Object> arguments) {
            this.type = type;
            this.arguments = unmodifiableList(new ArrayList<>(arguments));
            validateArguments(type, arguments);
        }

        protected void validateArguments(@Nonnull EventType type, @Nonnull Collection<Object> arguments) {
            final List<Class<?>> argumentTypes = type.argumentTypes();
            final Iterator<Class<?>> ati = argumentTypes.iterator();
            final Iterator<Object> ai = arguments.iterator();
            int i = 0;
            while (ati.hasNext() && ai.hasNext()) {
                final Class<?> currentType = ati.next();
                if (currentType.isPrimitive()) {
                    throw new IllegalArgumentException("Type " + type + " specified argument of type " + currentType + " at index #" + i + " which is a primitive." +
                        " This is not allowed for events.");
                }
                final Object currentArgument = ai.next();
                if (currentArgument != null && !currentType.isInstance(currentArgument)) {
                    throw new IllegalArgumentException("Expected argument of type " + currentType + " at index #" + i + " for event of type " + type +
                        " but got " + currentArgument.getClass().getName() + ": " + currentArgument);
                }
                i++;
            }
            if (ati.hasNext() || ai.hasNext()) {
                throw new IllegalArgumentException("Expected " + argumentTypes.size() + " arguments for an event of type " + type + " but got " + arguments.size() + ".");
            }
        }

        @Nonnull
        @Override
        public EventType type() {
            return type;
        }

        @Nonnull
        @Override
        public List<Object> arguments() {
            return arguments;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (!(o instanceof Event)) { return false; }
            final Event that = (Event) o;
            return Objects.equals(type(), that.type()) &&
                Objects.equals(arguments(), that.arguments());
        }

        @Override
        public int hashCode() {
            return Objects.hash(type(), arguments());
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            final EventType type = type();

            sb.append(type.returnType().getSimpleName()).append(": ");
            sb.append(type.sourceType().getSimpleName())
                .append("#")
                .append(type.name())
                .append('(');

            final Iterator<Class<?>> argumentTypes = type.argumentTypes().iterator();
            final Iterator<Object> arguments = arguments().iterator();

            boolean first = true;
            while (argumentTypes.hasNext() && arguments.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append("\n\t");
                sb.append(argumentTypes.next().getSimpleName())
                    .append(": ")
                    .append(arguments.next());
            }

            sb.append("\n)");

            return sb.toString();
        }

    }

}
