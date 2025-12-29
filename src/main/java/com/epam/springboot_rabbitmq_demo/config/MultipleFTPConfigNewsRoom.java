package com.epam.springboot_rabbitmq_demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.remote.aop.RotatingServerAdvice;
import org.springframework.integration.file.remote.aop.RotationPolicy;
import org.springframework.integration.file.remote.session.DelegatingSessionFactory;
import org.springframework.integration.ftp.dsl.Ftp;
import org.springframework.integration.ftp.filters.FtpPersistentAcceptOnceFileListFilter;
import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.metadata.SimpleMetadataStore;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class MultipleFTPConfigNewsRoom {

    private static final Logger logger = LoggerFactory.getLogger(MultipleFTPConfigNewsRoom.class);

    private IntegrationFlow flow;
    DefaultFtpSessionFactory factory;

    private DefaultFtpSessionFactory createFtpSessionFactory() {
        if (factory == null) {
            factory = new DefaultFtpSessionFactory();
            factory.setHost("localhost");
            factory.setPort(2121);
            factory.setUsername("anonymous");
            factory.setPassword("");
        }
        return factory;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setThreadNamePrefix("task-thread-");
        return taskExecutor;
    }

    @Bean(name = "newsroomIntegrationFlow")
    public IntegrationFlow flow() {

        return IntegrationFlow.from(Ftp.inboundAdapter(createFtpSessionFactory())
                                .filter(new FtpPersistentAcceptOnceFileListFilter(new SimpleMetadataStore(), "rotate"))
                                .filter(new FtpSimplePatternFileListFilter("*.zip"))
                                //.filterFunction(ftpFile -> ftpFile.getName().contains(".zip"))
                                .localDirectory(new File("/demo/directory"))
                                .autoCreateLocalDirectory(true)
                                .localFilenameExpression("#remoteDirectory + T(java.io.File).separator + #root")
                                .deleteRemoteFiles(true)
                                .remoteDirectory("."),
                        e -> e.poller(Pollers.fixedDelay(1).advice(advice())))
                .channel(MessageChannels.queue("newsroomfiles"))
                .handle((message,headers) -> {
                    logger.error("FTP error occurred: {}", message);
                    File file = (File) message;
                    String fullFtpPath = (String) headers.get("#remoteDirectory") + "/" + file.getName();
                    System.out.println("File received: " + fullFtpPath);
                    return null;
                })
                .get();

    }

    @Bean(name = "newsroomadvice")
    public RotatingServerAdvice advice() {
        return new RotatingServerAdvice(delegatingSessionFactory(), getKeyDirectories(), true);
    }

    @Bean(name = "newsroomdelegatingSessionFactory")
    public DelegatingSessionFactory delegatingSessionFactory() {

        Map<String, DefaultFtpSessionFactory> defaultFtpSessionFactoryMap = new HashMap<>();
        defaultFtpSessionFactoryMap.put("newsroom", createFtpSessionFactory());

        return new DelegatingSessionFactory(defaultFtpSessionFactoryMap, createFtpSessionFactory());
    }

    private List<RotationPolicy.KeyDirectory> getKeyDirectories() {
        List<RotationPolicy.KeyDirectory> keyDirectories = new ArrayList<>();
        keyDirectories.add(new RotationPolicy.KeyDirectory("Newsroom3", "/20250211/Newsroom3"));
        keyDirectories.add(new RotationPolicy.KeyDirectory("Newsroom1", "/20250211/Newsroom1"));
        keyDirectories.add(new RotationPolicy.KeyDirectory("Newsroom2", "/20250211/Newsroom2"));
        return keyDirectories;
    }
}
