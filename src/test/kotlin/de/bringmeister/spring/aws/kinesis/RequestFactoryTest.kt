package de.bringmeister.spring.aws.kinesis

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RequestFactoryTest {

    val objectMapper = ObjectMapper()
    val requestFactory = RequestFactory(objectMapper)
    val event = Record(FooCreatedEvent("any-value"), EventMetadata("test"));

    @Test
    fun `should use event stream name for request`() {
        val request = requestFactory.request("foo-stream", event)
        assertThat(request.streamName).isEqualTo("foo-stream")
    }

    @Test
    fun `should add a random partition key`() {
        val request = requestFactory.request("foo-stream", event)
        assertThat(request.records[0].partitionKey).isNotNull()
    }

    @Test
    fun `should serialize message and meta data`() {
        val request = requestFactory.request("foo-stream", event)
        val content = String(request.records[0].data.array())
        assertThat(content).isEqualTo("""{"data":{"foo":"any-value"},"metadata":{"sender":"test"}}""")
    }
}