package com.jkliu.tinyapp;

import com.jkliu.tinyapp.core.Config;
import com.jkliu.tinyapp.utils.FileProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

@SpringBootApplication
public class TinyAppApplication {
	private static final Logger logger = LogManager.getLogger("TinyAppApplication");

	public static void main(String[] args) {
		Config.init();
		SpringApplication.run(TinyAppApplication.class, args);
	}

}
