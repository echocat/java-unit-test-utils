package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
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
        return new Default(type, arguments);
    }

    @Immutable
    class Default implements Event {

        @Nonnull
        private final EventType type;
        @Nonnull
        private final List<Object> arguments;

        protected Default(@Nonnull EventType type, @Nonnull Object... arguments) {
            this.type = type;
            this.arguments = unmodifiableList(asList(arguments));
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
