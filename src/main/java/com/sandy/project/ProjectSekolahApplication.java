package com.sandy.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class ProjectSekolahApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectSekolahApplication.class, args);
	}
}

