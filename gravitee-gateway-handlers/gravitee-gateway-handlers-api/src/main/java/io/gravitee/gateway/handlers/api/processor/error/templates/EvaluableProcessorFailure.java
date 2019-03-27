package io.gravitee.gateway.handlers.api.processor.error.templates;

import io.gravitee.gateway.core.processor.ProcessorFailure;

import java.util.Map;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class EvaluableProcessorFailure {

    private final ProcessorFailure failure;

    public EvaluableProcessorFailure(final ProcessorFailure failure) {
        this.failure = failure;
    }

    public int getStatusCode() {
        return failure.statusCode();
    }

    public String getKey() {
        return failure.key();
    }

    public String getMessage() {
        return failure.message();
    }

    public Map<String, Object> getParameters() {
        return failure.parameters();
    }
}
