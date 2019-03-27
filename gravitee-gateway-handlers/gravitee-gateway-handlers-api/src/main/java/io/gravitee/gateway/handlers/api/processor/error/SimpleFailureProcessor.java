package io.gravitee.gateway.handlers.api.processor.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gravitee.common.http.HttpHeaders;
import io.gravitee.common.http.HttpHeadersValues;
import io.gravitee.common.http.MediaType;
import io.gravitee.gateway.api.ExecutionContext;
import io.gravitee.gateway.api.Response;
import io.gravitee.gateway.api.buffer.Buffer;
import io.gravitee.gateway.core.processor.AbstractProcessor;
import io.gravitee.gateway.core.processor.ProcessorFailure;

public class SimpleFailureProcessor extends AbstractProcessor<ExecutionContext> {

    private final static ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(ExecutionContext context) {
        final ProcessorFailure failure = (ProcessorFailure) context.getAttribute(ExecutionContext.ATTR_PREFIX + "failure");

        prepareResponse(context.response(), failure);
        handleFailure(context, failure);
    }

    protected void handleFailure(final ExecutionContext context, final ProcessorFailure failure) {
        final Response response = context.response();

        if (failure.message() != null) {
            try {
                String contentAsJson = mapper.writeValueAsString(new ProcessorFailureAsJson(failure));
                Buffer payload = Buffer.buffer(contentAsJson);
                response.headers().set(HttpHeaders.CONTENT_LENGTH, Integer.toString(payload.length()));
                response.headers().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                response.write(payload);
            } catch (JsonProcessingException jpe) {
                // Do nothing
            }
        }
    }

    protected void prepareResponse(final Response response, final ProcessorFailure failure) {
        response.status(failure.statusCode());
        response.headers().set(HttpHeaders.CONNECTION, HttpHeadersValues.CONNECTION_CLOSE);
    }

    private class ProcessorFailureAsJson {

        @JsonProperty
        private final String message;

        @JsonProperty("http_status_code")
        private final int httpStatusCode;

        private ProcessorFailureAsJson(ProcessorFailure processorFailure) {
            this.message = processorFailure.message();
            this.httpStatusCode = processorFailure.statusCode();
        }

        private String getMessage() {
            return message;
        }

        private int httpStatusCode() {
            return httpStatusCode;
        }
    }
}
