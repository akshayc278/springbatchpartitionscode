package com.aktechie.base.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")

public class JobController {

	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;
	
	@PostMapping("/importmap")
	public void importCSV() throws Exception {
		JobParameters jobParameter1=new JobParametersBuilder()
				.addLong("startall", System.currentTimeMillis()).toJobParameters();
		
		jobLauncher.run(job, jobParameter1);
	}
	
}
