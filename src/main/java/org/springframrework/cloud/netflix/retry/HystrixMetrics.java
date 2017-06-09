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

package org.springframrework.cloud.netflix.retry;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Dave Syer
 *
 */
public class HystrixMetrics {

	private String threadPool = "Spring";
	private int reportingHosts = 1;
	private boolean propertyValue_requestLogEnabled = true;
	private boolean propertyValue_requestCacheEnabled = true;
	private long propertyValue_metricsRollingStatisticalWindowInMilliseconds = 10000;
	private int propertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests = 10;
	private int propertyValue_executionIsolationSemaphoreMaxConcurrentRequests = 10;
	private String propertyValue_executionIsolationThreadPoolKeyOverride = null;
	private boolean propertyValue_executionIsolationThreadInterruptOnTimeout = true;
	private long propertyValue_executionTimeoutInMilliseconds = 60000;
	private long propertyValue_executionIsolationThreadTimeoutInMilliseconds = 60000;
	private String propertyValue_executionIsolationStrategy = "SEMAPHORE";
	private boolean propertyValue_circuitBreakerEnabled = true;
	private boolean propertyValue_circuitBreakerForceClosed = false;
	private boolean propertyValue_circuitBreakerForceOpen = false;
	private int propertyValue_circuitBreakerErrorThresholdPercentage = 50;
	private int propertyValue_circuitBreakerRequestVolumeThreshold = 20;
	private long propertyValue_circuitBreakerSleepWindowInMilliseconds = 5000;
	private long rollingCountFallbackMissing = 0;
	private long rollingCountFallbackFailure = 0;
	private long rollingCountFallbackEmit = 0;
	private long rollingCountFailure = 0;
	private long rollingCountExceptionsThrown = 0;
	private long rollingCountEmit = 0;
	private long rollingCountCollapsedRequests = 0;
	private long rollingCountBadRequests = 0;
	private String type = "HystrixCommand";
	private String name = "command";
	private String group = "Spring";
	private long currentTime = System.currentTimeMillis();
	@JsonProperty("isCircuitBreakerOpen")
	private boolean isCircuitBreakerOpen = false;
	private double errorPercentage = 0;
	private long errorCount = 0;
	private long requestCount = 1;
	private long rollingCountFallbackRejection = 0;
	private long rollingCountFallbackSuccess = 0;
	private long rollingCountResponsesFromCache = 0;
	private long rollingCountSemaphoreRejected = 0;
	private long rollingCountShortCircuited = 0;
	private long rollingCountSuccess = 0;
	private long rollingCountThreadPoolRejected = 0;
	private long rollingCountTimeout = 0;
	private long rollingMaxConcurrentExecutionCount = 0;
	private long currentConcurrentExecutionCount = 0;
	private long latencyExecute_mean = 0;
	private Latency latencyExecute = new Latency();
	private long latencyTotal_mean = 0;
	private Latency latencyTotal = new Latency();

	public String getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(String threadPool) {
		this.threadPool = threadPool;
	}

	public int getReportingHosts() {
		return reportingHosts;
	}

	public void setReportingHosts(int reportingHosts) {
		this.reportingHosts = reportingHosts;
	}

	public boolean isPropertyValue_requestLogEnabled() {
		return propertyValue_requestLogEnabled;
	}

	public void setPropertyValue_requestLogEnabled(boolean propertyValue_requestLogEnabled) {
		this.propertyValue_requestLogEnabled = propertyValue_requestLogEnabled;
	}

	public boolean isPropertyValue_requestCacheEnabled() {
		return propertyValue_requestCacheEnabled;
	}

	public void setPropertyValue_requestCacheEnabled(
			boolean propertyValue_requestCacheEnabled) {
		this.propertyValue_requestCacheEnabled = propertyValue_requestCacheEnabled;
	}

	public long getPropertyValue_metricsRollingStatisticalWindowInMilliseconds() {
		return propertyValue_metricsRollingStatisticalWindowInMilliseconds;
	}

	public void setPropertyValue_metricsRollingStatisticalWindowInMilliseconds(
			long propertyValue_metricsRollingStatisticalWindowInMilliseconds) {
		this.propertyValue_metricsRollingStatisticalWindowInMilliseconds = propertyValue_metricsRollingStatisticalWindowInMilliseconds;
	}

	public int getPropertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests() {
		return propertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests;
	}

	public void setPropertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests(
			int propertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests) {
		this.propertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests = propertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests;
	}

	public int getPropertyValue_executionIsolationSemaphoreMaxConcurrentRequests() {
		return propertyValue_executionIsolationSemaphoreMaxConcurrentRequests;
	}

	public void setPropertyValue_executionIsolationSemaphoreMaxConcurrentRequests(
			int propertyValue_executionIsolationSemaphoreMaxConcurrentRequests) {
		this.propertyValue_executionIsolationSemaphoreMaxConcurrentRequests = propertyValue_executionIsolationSemaphoreMaxConcurrentRequests;
	}

	public String getPropertyValue_executionIsolationThreadPoolKeyOverride() {
		return propertyValue_executionIsolationThreadPoolKeyOverride;
	}

	public void setPropertyValue_executionIsolationThreadPoolKeyOverride(
			String propertyValue_executionIsolationThreadPoolKeyOverride) {
		this.propertyValue_executionIsolationThreadPoolKeyOverride = propertyValue_executionIsolationThreadPoolKeyOverride;
	}

	public boolean isPropertyValue_executionIsolationThreadInterruptOnTimeout() {
		return propertyValue_executionIsolationThreadInterruptOnTimeout;
	}

	public void setPropertyValue_executionIsolationThreadInterruptOnTimeout(
			boolean propertyValue_executionIsolationThreadInterruptOnTimeout) {
		this.propertyValue_executionIsolationThreadInterruptOnTimeout = propertyValue_executionIsolationThreadInterruptOnTimeout;
	}

	public long getPropertyValue_executionTimeoutInMilliseconds() {
		return propertyValue_executionTimeoutInMilliseconds;
	}

	public void setPropertyValue_executionTimeoutInMilliseconds(
			long propertyValue_executionTimeoutInMilliseconds) {
		this.propertyValue_executionTimeoutInMilliseconds = propertyValue_executionTimeoutInMilliseconds;
	}

	public long getPropertyValue_executionIsolationThreadTimeoutInMilliseconds() {
		return propertyValue_executionIsolationThreadTimeoutInMilliseconds;
	}

	public void setPropertyValue_executionIsolationThreadTimeoutInMilliseconds(
			long propertyValue_executionIsolationThreadTimeoutInMilliseconds) {
		this.propertyValue_executionIsolationThreadTimeoutInMilliseconds = propertyValue_executionIsolationThreadTimeoutInMilliseconds;
	}

	public String getPropertyValue_executionIsolationStrategy() {
		return propertyValue_executionIsolationStrategy;
	}

	public void setPropertyValue_executionIsolationStrategy(
			String propertyValue_executionIsolationStrategy) {
		this.propertyValue_executionIsolationStrategy = propertyValue_executionIsolationStrategy;
	}

	public boolean isPropertyValue_circuitBreakerEnabled() {
		return propertyValue_circuitBreakerEnabled;
	}

	public void setPropertyValue_circuitBreakerEnabled(
			boolean propertyValue_circuitBreakerEnabled) {
		this.propertyValue_circuitBreakerEnabled = propertyValue_circuitBreakerEnabled;
	}

	public boolean isPropertyValue_circuitBreakerForceClosed() {
		return propertyValue_circuitBreakerForceClosed;
	}

	public void setPropertyValue_circuitBreakerForceClosed(
			boolean propertyValue_circuitBreakerForceClosed) {
		this.propertyValue_circuitBreakerForceClosed = propertyValue_circuitBreakerForceClosed;
	}

	public boolean isPropertyValue_circuitBreakerForceOpen() {
		return propertyValue_circuitBreakerForceOpen;
	}

	public void setPropertyValue_circuitBreakerForceOpen(
			boolean propertyValue_circuitBreakerForceOpen) {
		this.propertyValue_circuitBreakerForceOpen = propertyValue_circuitBreakerForceOpen;
	}

	public int getPropertyValue_circuitBreakerErrorThresholdPercentage() {
		return propertyValue_circuitBreakerErrorThresholdPercentage;
	}

	public void setPropertyValue_circuitBreakerErrorThresholdPercentage(
			int propertyValue_circuitBreakerErrorThresholdPercentage) {
		this.propertyValue_circuitBreakerErrorThresholdPercentage = propertyValue_circuitBreakerErrorThresholdPercentage;
	}

	public int getPropertyValue_circuitBreakerRequestVolumeThreshold() {
		return propertyValue_circuitBreakerRequestVolumeThreshold;
	}

	public void setPropertyValue_circuitBreakerRequestVolumeThreshold(
			int propertyValue_circuitBreakerRequestVolumeThreshold) {
		this.propertyValue_circuitBreakerRequestVolumeThreshold = propertyValue_circuitBreakerRequestVolumeThreshold;
	}

	public long getPropertyValue_circuitBreakerSleepWindowInMilliseconds() {
		return propertyValue_circuitBreakerSleepWindowInMilliseconds;
	}

	public void setPropertyValue_circuitBreakerSleepWindowInMilliseconds(
			long propertyValue_circuitBreakerSleepWindowInMilliseconds) {
		this.propertyValue_circuitBreakerSleepWindowInMilliseconds = propertyValue_circuitBreakerSleepWindowInMilliseconds;
	}

	public long getRollingCountFallbackMissing() {
		return rollingCountFallbackMissing;
	}

	public void setRollingCountFallbackMissing(long rollingCountFallbackMissing) {
		this.rollingCountFallbackMissing = rollingCountFallbackMissing;
	}

	public long getRollingCountFallbackFailure() {
		return rollingCountFallbackFailure;
	}

	public void setRollingCountFallbackFailure(long rollingCountFallbackFailure) {
		this.rollingCountFallbackFailure = rollingCountFallbackFailure;
	}

	public long getRollingCountFallbackEmit() {
		return rollingCountFallbackEmit;
	}

	public void setRollingCountFallbackEmit(long rollingCountFallbackEmit) {
		this.rollingCountFallbackEmit = rollingCountFallbackEmit;
	}

	public long getRollingCountFailure() {
		return rollingCountFailure;
	}

	public void setRollingCountFailure(long rollingCountFailure) {
		this.rollingCountFailure = rollingCountFailure;
	}

	public long getRollingCountExceptionsThrown() {
		return rollingCountExceptionsThrown;
	}

	public void setRollingCountExceptionsThrown(long rollingCountExceptionsThrown) {
		this.rollingCountExceptionsThrown = rollingCountExceptionsThrown;
	}

	public long getRollingCountEmit() {
		return rollingCountEmit;
	}

	public void setRollingCountEmit(long rollingCountEmit) {
		this.rollingCountEmit = rollingCountEmit;
	}

	public long getRollingCountCollapsedRequests() {
		return rollingCountCollapsedRequests;
	}

	public void setRollingCountCollapsedRequests(long rollingCountCollapsedRequests) {
		this.rollingCountCollapsedRequests = rollingCountCollapsedRequests;
	}

	public long getRollingCountBadRequests() {
		return rollingCountBadRequests;
	}

	public void setRollingCountBadRequests(long rollingCountBadRequests) {
		this.rollingCountBadRequests = rollingCountBadRequests;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public boolean isCircuitBreakerOpen() {
		return isCircuitBreakerOpen;
	}

	public void setCircuitBreakerOpen(boolean isCircuitBreakerOpen) {
		this.isCircuitBreakerOpen = isCircuitBreakerOpen;
	}

	public double getErrorPercentage() {
		return errorPercentage;
	}

	public void setErrorPercentage(double errorPercentage) {
		this.errorPercentage = errorPercentage;
	}

	public long getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(long errorCount) {
		this.errorCount = errorCount;
	}

	public long getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(long requestCount) {
		this.requestCount = requestCount;
	}

	public long getRollingCountFallbackRejection() {
		return rollingCountFallbackRejection;
	}

	public void setRollingCountFallbackRejection(long rollingCountFallbackRejection) {
		this.rollingCountFallbackRejection = rollingCountFallbackRejection;
	}

	public long getRollingCountFallbackSuccess() {
		return rollingCountFallbackSuccess;
	}

	public void setRollingCountFallbackSuccess(long rollingCountFallbackSuccess) {
		this.rollingCountFallbackSuccess = rollingCountFallbackSuccess;
	}

	public long getRollingCountResponsesFromCache() {
		return rollingCountResponsesFromCache;
	}

	public void setRollingCountResponsesFromCache(long rollingCountResponsesFromCache) {
		this.rollingCountResponsesFromCache = rollingCountResponsesFromCache;
	}

	public long getRollingCountSemaphoreRejected() {
		return rollingCountSemaphoreRejected;
	}

	public void setRollingCountSemaphoreRejected(long rollingCountSemaphoreRejected) {
		this.rollingCountSemaphoreRejected = rollingCountSemaphoreRejected;
	}

	public long getRollingCountShortCircuited() {
		return rollingCountShortCircuited;
	}

	public void setRollingCountShortCircuited(long rollingCountShortCircuited) {
		this.rollingCountShortCircuited = rollingCountShortCircuited;
	}

	public long getRollingCountSuccess() {
		return rollingCountSuccess;
	}

	public void setRollingCountSuccess(long rollingCountSuccess) {
		this.rollingCountSuccess = rollingCountSuccess;
	}

	public long getRollingCountThreadPoolRejected() {
		return rollingCountThreadPoolRejected;
	}

	public void setRollingCountThreadPoolRejected(long rollingCountThreadPoolRejected) {
		this.rollingCountThreadPoolRejected = rollingCountThreadPoolRejected;
	}

	public long getRollingCountTimeout() {
		return rollingCountTimeout;
	}

	public void setRollingCountTimeout(long rollingCountTimeout) {
		this.rollingCountTimeout = rollingCountTimeout;
	}

	public long getCurrentConcurrentExecutionCount() {
		return currentConcurrentExecutionCount;
	}

	public void setCurrentConcurrentExecutionCount(long currentConcurrentExecutionCount) {
		this.currentConcurrentExecutionCount = currentConcurrentExecutionCount;
	}

	public long getRollingMaxConcurrentExecutionCount() {
		return rollingMaxConcurrentExecutionCount;
	}

	public void setRollingMaxConcurrentExecutionCount(
			long rollingMaxConcurrentExecutionCount) {
		this.rollingMaxConcurrentExecutionCount = rollingMaxConcurrentExecutionCount;
	}

	public long getLatencyExecute_mean() {
		return latencyExecute_mean;
	}

	public void setLatencyExecute_mean(long latencyExecute_mean) {
		this.latencyExecute_mean = latencyExecute_mean;
	}

	public Latency getLatencyExecute() {
		return latencyExecute;
	}

	public void setLatencyExecute(Latency latencyExecute) {
		this.latencyExecute = latencyExecute;
	}

	public long getLatencyTotal_mean() {
		return latencyTotal_mean;
	}

	public void setLatencyTotal_mean(long latencyTotal_mean) {
		this.latencyTotal_mean = latencyTotal_mean;
	}

	public Latency getLatencyTotal() {
		return latencyTotal;
	}

	public void setLatencyTotal(Latency latencyTotal) {
		this.latencyTotal = latencyTotal;
	}

	public static class Latency {
		@JsonProperty("100")
		private long v100 = 0;
		@JsonProperty("0")
		private long v0 = 0;
		@JsonProperty("20")
		private long v25 = 0;
		@JsonProperty("50")
		private long v50 = 0;
		@JsonProperty("75")
		private long v75 = 0;
		@JsonProperty("90")
		private long v90 = 0;
		@JsonProperty("95")
		private long v95 = 0;
		@JsonProperty("99")
		private long v99 = 0;
		@JsonProperty("99.5")
	    private long v995=0;
	}
}
