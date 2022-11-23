package net.wmann.fileanalyser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Value("${task.pool.nthread:4}")
	public ExecutorService threadPoolTaskExecutor(int nThread) {
		return Executors.newFixedThreadPool(nThread);
	}

}
