/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.netflix.retry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.SmartLifecycle;
import org.springframework.core.AttributeAccessor;
import org.springframework.retry.RetryStatistics;
import org.springframework.retry.policy.CircuitBreakerRetryPolicy;
import org.springframework.retry.stats.ExponentialAverageRetryStatistics;
import org.springframework.retry.stats.StatisticsRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dave Syer
 *
 */
@RestController
public class HystrixStreamController implements SmartLifecycle {

	private static Log logger = LogFactory.getLog(HystrixStreamController.class);

	private long delay = 500;

	private boolean running = false;

	private final StatisticsRepository repository;

	private final ObjectMapper objectMapper;

	public HystrixStreamController(StatisticsRepository repository,
			ObjectMapper objectMapper) {
		this.repository = repository;
		this.objectMapper = objectMapper;
	}

	@RequestMapping(path = "/hystrix.stream", produces = "text/event-stream")
	public void handle(HttpServletResponse response) {
		response.setHeader("Content-Type", "text/event-stream;charset=UTF-8");
		response.setHeader("Cache-Control",
				"no-cache, no-store, max-age=0, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		try {
			while (running) {
				List<String> jsonMessages = fetchMetrics();
				if (jsonMessages.isEmpty()) {
					response.getWriter().println("ping: \n");
				}
				else {
					for (String json : jsonMessages) {
						response.getWriter().println("data: " + json + "\n");
					}
				}

				/* shortcut breaking out of loop if we have been destroyed */
				if (!running) {
					break;
				}

				// after outputting all the messages we will flush the stream
				response.flushBuffer();

				// explicitly check for client disconnect - PrintWriter does not throw
				// exceptions
				if (response.getWriter().checkError()) {
					throw new IOException("io error");
				}

				// now wait the 'delay' time
				Thread.sleep(delay);
			}
		}
		catch (InterruptedException e) {
			stop();
			logger.debug("InterruptedException.");
			Thread.currentThread().interrupt();
		}
		catch (IOException e) {
			// debug instead of error as we expect to get these whenever a client
			// disconnects or network issue occurs
			logger.debug(
					"IOException while trying to write (generally caused by client disconnecting).",
					e);
		}
		catch (Exception e) {
			logger.error("Failed to write Hystrix metrics.", e);
		}
		logger.debug("Stopping stream to connection");

	}

	private List<String> fetchMetrics() throws JsonProcessingException {
		List<String> list = new ArrayList<>();
		for (RetryStatistics stats : repository.findAll()) {
			if (stats.getStartedCount() > 0) {
				HystrixMetrics metrics = new HystrixMetrics();
				metrics.setName(stats.getName());
				metrics.setErrorCount(stats.getErrorCount());
				metrics.setRequestCount(getRollingStartedCount(stats));
				metrics.setErrorPercentage(getRollingErrorRate(stats) * 100);
				metrics.setCurrentConcurrentExecutionCount(getConcurrentCount(stats));
				metrics.setRollingCountFailure(getRollingFailureCount(stats));
				metrics.setRollingCountSuccess(getRollingSuccessCount(stats));
				metrics.setRollingCountShortCircuited(
						getRollingFallbackShortCircuitedCount(stats));
				metrics.setRollingCountFallbackSuccess(
						getRollingFallbackSuccessCount(stats));
				metrics.setCircuitBreakerOpen(isCircuitBreakerOpen(stats));
				list.add(objectMapper.writeValueAsString(metrics));
			}
		}
		return list;
	}

	private double getRollingErrorRate(RetryStatistics stats) {
		if (stats instanceof ExponentialAverageRetryStatistics) {
			ExponentialAverageRetryStatistics average = (ExponentialAverageRetryStatistics) stats;
			return average.getRollingErrorRate();
		}
		return 0;
	}

	private long getConcurrentCount(RetryStatistics stats) {
		return 0;
	}

	private boolean isCircuitBreakerOpen(RetryStatistics stats) {
		if (stats instanceof AttributeAccessor) {
			Object attribute = ((AttributeAccessor) stats)
					.getAttribute(CircuitBreakerRetryPolicy.CIRCUIT_OPEN);
			return attribute == null ? false : (Boolean) attribute;
		}
		return false;
	}

	private int getRollingFallbackShortCircuitedCount(RetryStatistics stats) {
		if (stats instanceof AttributeAccessor) {
			Object attribute = ((AttributeAccessor) stats)
					.getAttribute(CircuitBreakerRetryPolicy.CIRCUIT_SHORT_COUNT);
			return attribute == null ? 0 : (Integer) attribute;
		}
		return 0;
	}

	private int getRollingFallbackSuccessCount(RetryStatistics stats) {
		if (stats instanceof ExponentialAverageRetryStatistics) {
			ExponentialAverageRetryStatistics average = (ExponentialAverageRetryStatistics) stats;
			return average.getRollingRecoveryCount();
		}
		return 0;
	}

	private int getRollingStartedCount(RetryStatistics stats) {
		if (stats instanceof ExponentialAverageRetryStatistics) {
			ExponentialAverageRetryStatistics average = (ExponentialAverageRetryStatistics) stats;
			return average.getRollingStartedCount();
		}
		return 0;
	}

	private int getRollingSuccessCount(RetryStatistics stats) {
		return getRollingStartedCount(stats) - getRollingFailureCount(stats);
	}

	private int getRollingFailureCount(RetryStatistics stats) {
		if (stats instanceof ExponentialAverageRetryStatistics) {
			ExponentialAverageRetryStatistics average = (ExponentialAverageRetryStatistics) stats;
			return average.getRollingAbortCount() + getRollingFallbackSuccessCount(stats);
		}
		return 0;
	}

	@Override
	public void start() {
		running = true;
	}

	@Override
	public void stop() {
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		callback.run();
	}

}
