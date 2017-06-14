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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.actuate.endpoint.mvc.AbstractNamedMvcEndpoint;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.AttributeAccessor;
import org.springframework.http.MediaType;
import org.springframework.retry.RetryStatistics;
import org.springframework.retry.policy.CircuitBreakerRetryPolicy;
import org.springframework.retry.stats.ExponentialAverageRetryStatistics;
import org.springframework.retry.stats.StatisticsRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author Dave Syer
 *
 */
@Component
public class HystrixStreamEndpoint extends AbstractNamedMvcEndpoint
		implements SmartLifecycle {

	private static Log logger = LogFactory.getLog(HystrixStreamEndpoint.class);

	private long delay = 500;

	private AtomicBoolean running = new AtomicBoolean(false);

	private final StatisticsRepository repository;

	private final ObjectMapper objectMapper;

	private final List<SseEmitter> emitters = new ArrayList<>();

	public HystrixStreamEndpoint(StatisticsRepository repository,
			ObjectMapper objectMapper) {
		super("hystrix", "/hystrix.stream", false);
		this.repository = repository;
		this.objectMapper = objectMapper;
	}

	@RequestMapping(path = "", produces = "text/event-stream")
	public SseEmitter handle() {
		// No timeout, otherwise the container will disconnect the client
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		emitters.add(emitter);
		return emitter;

	}

	private List<String> fetchMetrics() throws JsonProcessingException {
		List<String> list = new ArrayList<>();
		for (RetryStatistics stats : repository.findAll()) {
			if (stats.getStartedCount() > 0) {
				HystrixMetrics metrics = new HystrixMetrics();
				metrics.setName(stats.getName());
				metrics.setErrorCount(stats.getErrorCount());
				// The "request count" actually gets divided by the window size to
				// calculate a rate in the hystrix dashboard, so it's the rolling value we
				// need here, not the total count:
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
		if (running.compareAndSet(false, true)) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (running.get()) {
						List<SseEmitter> emitters;
						synchronized (this) {
							emitters = new ArrayList<>(
									HystrixStreamEndpoint.this.emitters);
						}
						for (SseEmitter emitter : emitters) {
							try {
								List<String> jsonMessages = fetchMetrics();
								if (jsonMessages.isEmpty()) {
									emitter.send(SseEmitter.event().name("ping"));
								}
								else {
									for (String json : jsonMessages) {
										emitter.send(SseEmitter.event().data(json,
												MediaType.TEXT_PLAIN));
									}
								}
							}
							catch (Exception e) {
								logger.debug(
										"Failed to write Hystrix metrics, disconnecting client.",
										e);
								synchronized (this) {
									HystrixStreamEndpoint.this.emitters
											.remove(HystrixStreamEndpoint.this.emitters
													.indexOf(emitter));
								}
							}
							if (!running.get()) {
								break;
							}
						}
						try {
							// now wait the 'delay' time
							Thread.sleep(delay);
						}
						catch (InterruptedException e) {
							stop();
							logger.debug("InterruptedException.");
							Thread.currentThread().interrupt();
						}
					}

					logger.debug("Stopping stream to connection");
				}
			}, "hystrixEmitters");
			thread.start();
		}
	}

	@Override
	public void stop() {
		if (running.compareAndSet(true, false)) {
			synchronized (this) {
				this.emitters.clear();
			}
		}
	}

	@Override
	public boolean isRunning() {
		return running.get();
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
