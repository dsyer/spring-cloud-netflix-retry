package org.springframework.cloud.netflix.retry;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpringCloudNetflixRetryApplicationTests {

	private static Log log = LogFactory
			.getLog(SpringCloudNetflixRetryApplicationTests.class);

	@Autowired
	ObjectMapper mapper;

	RestTemplate rest = new RestTemplate();

	@LocalServerPort
	private int port;

	private CountDownLatch latch = new CountDownLatch(1);

	@Test
	public void contextLoads() throws Exception {
		rest.getInterceptors().add(new NonClosingInterceptor());
		rest.getForEntity("http://localhost:" + port + "/all", String.class);
		ResponseEntity<String> response = rest.execute(
				new URI("http://localhost:" + port + "/hystrix.stream"), HttpMethod.GET,
				null, this::extract);
		assertThat(response.getHeaders().getContentType())
				.isGreaterThan(MediaType.TEXT_EVENT_STREAM);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Map<String, Object> metrics = extractMetrics(response.getBody());
		assertThat(metrics).containsEntry("type", "HystrixCommand");
		System.err.println(metrics);
	}

	private ResponseEntity<String> extract(ClientHttpResponse response)
			throws IOException {
		byte[] bytes = new byte[1024];
		StringBuilder builder = new StringBuilder();
		int read = 0;
		while (read >= 0
				&& StringUtils.countOccurrencesOf(builder.toString(), "\n") < 2) {
			read = response.getBody().read(bytes, 0, bytes.length);
			if (read > 0) {
				latch.countDown();
				builder.append(new String(bytes, 0, read));
			}
			log.debug("Building: " + builder);
		}
		log.debug("Done: " + builder);
		return ResponseEntity.status(response.getStatusCode())
				.headers(response.getHeaders()).body(builder.toString());
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> extractMetrics(String body) throws Exception {
		String[] split = body.split("data:");
		for (String value : split) {
			if (value.contains("Ping") || value.length() == 0) {
				continue;
			}
			else {
				return mapper.readValue(value, Map.class);
			}
		}
		return null;
	}

	/**
	 * Special interceptor that prevents the response from being closed and allows us to
	 * assert on the contents of an event stream.
	 */
	private class NonClosingInterceptor implements ClientHttpRequestInterceptor {

		private class NonClosingResponse implements ClientHttpResponse {

			private ClientHttpResponse delegate;

			public NonClosingResponse(ClientHttpResponse delegate) {
				this.delegate = delegate;
			}

			@Override
			public InputStream getBody() throws IOException {
				return delegate.getBody();
			}

			@Override
			public HttpHeaders getHeaders() {
				return delegate.getHeaders();
			}

			@Override
			public HttpStatus getStatusCode() throws IOException {
				return delegate.getStatusCode();
			}

			@Override
			public int getRawStatusCode() throws IOException {
				return delegate.getRawStatusCode();
			}

			@Override
			public String getStatusText() throws IOException {
				return delegate.getStatusText();
			}

			@Override
			public void close() {
			}

		}

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body,
				ClientHttpRequestExecution execution) throws IOException {
			return new NonClosingResponse(execution.execute(request, body));
		}

	}
}
