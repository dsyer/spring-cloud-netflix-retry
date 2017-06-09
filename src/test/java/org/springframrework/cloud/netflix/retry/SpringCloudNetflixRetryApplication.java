package org.springframrework.cloud.netflix.retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryStatistics;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.policy.MapRetryContextCache;
import org.springframework.retry.policy.RetryContextCache;
import org.springframework.retry.stats.DefaultStatisticsRepository;
import org.springframework.retry.stats.StatisticsListener;
import org.springframework.retry.stats.StatisticsRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableRetry(proxyTargetClass = true)
public class SpringCloudNetflixRetryApplication {

	@Bean
	public StatisticsRepository statisticsRepository() {
		return new DefaultStatisticsRepository();
	}

	@Bean
	public RetryContextCache retryContextCache() {
		return new MapRetryContextCache();
	}

	@Bean
	public RetryListener retryStatisticsListener(StatisticsRepository repository) {
		return new StatisticsListener(repository);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudNetflixRetryApplication.class, args);
	}
}

@RestController
class MyController {

	@Autowired
	private StatisticsRepository repository;

	@CircuitBreaker(label = "home")
	@RequestMapping("/")
	Iterable<RetryStatistics> home() {
		return repository.findAll();
	}

	@CircuitBreaker(label = "home")
	@RequestMapping("/fail")
	Iterable<RetryStatistics> error() {
		throw new RuntimeException("Planned");
	}

	@CircuitBreaker(label = "home")
	@RequestMapping("/recover")
	String recover() {
		throw new RuntimeException("Planned");
	}
	
	@Recover
	String fallback() {
		return "Recovery";
	}

}
