package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.extensions.TemporaryResources.Resource.Scope;
import org.echocat.unittest.utils.nio.TemporaryResourceBroker;
import org.echocat.unittest.utils.utils.ThrowableCombiner;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;
import java.util.LinkedList;
import java.util.List;

import static org.echocat.unittest.utils.extensions.TemporaryResources.Resource.Scope.all;
import static org.echocat.unittest.utils.extensions.TemporaryResources.Resource.Scope.each;

public class TemporaryResources implements AfterAllCallback, AfterEachCallback {

    @Nonnull
    @GuardedBy("this")
    private final List<Resource> resources = new LinkedList<>();

    @Override
    public synchronized void afterEach(ExtensionContext context) throws Exception {
        after(each, context);
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) throws Exception {
        after(all, context);
    }

    protected synchronized void after(@Nonnull Scope scope, @Nonnull ExtensionContext context) throws Exception {
        final ThrowableCombiner combiner = new ThrowableCombiner();
        resources().stream()
            .filter(candidate -> candidate.scope() == scope)
            .map(Resource::broker)
            .forEach(candidate -> combiner.executeAndRecordIfAny(candidate::close));
        combiner.throwCheckedIfAny();
    }

    @Nonnull
    protected List<Resource> resources() {
        return resources;
    }

    protected static class Resource {
        @Nonnull
        private final TemporaryResourceBroker broker;
        @Nonnull
        private final Scope scope;

        protected Resource(@Nonnull TemporaryResourceBroker broker, @Nonnull Scope scope) {
            this.broker = broker;
            this.scope = scope;
        }

        @Nonnull
        public TemporaryResourceBroker broker() {
            return broker;
        }

        @Nonnull
        public Scope scope() {
            return scope;
        }

        protected enum Scope {
            each,
            all
        }

    }
}
