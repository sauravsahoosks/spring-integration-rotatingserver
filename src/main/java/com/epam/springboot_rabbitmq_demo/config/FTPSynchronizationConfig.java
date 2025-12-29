//package com.epam.springboot_rabbitmq_demo.config;
//
////import com.epam.springboot_rabbitmq_demo.publisher.RabbitMqProducer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.InboundChannelAdapter;
//import org.springframework.integration.annotation.Poller;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.core.MessageSource;
//import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;
//import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
//import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizingMessageSource;
//import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.io.File;
//import java.util.Arrays;
//import java.util.List;
//
//
//@Configuration
//public class FTPSynchronizationConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(FTPSynchronizationConfig.class);
//
////    @Autowired
////    private RabbitMqProducer rabbitMqProducer;
//
//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Autowired
//    private Job processFilesJob;
//
//    private FtpInboundFileSynchronizer ftpInboundFileSynchronizer;
//
//    // Define a list of directories to be monitored
//    private static final List<String> REMOTE_DIRECTORIES = Arrays.asList(
//            "/20250211/Newsroom/Folder1"
//            // Add more folders as needed
//    );
//
//    @Bean
//    public DefaultFtpSessionFactory sf() {
//        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
//        sf.setHost("localhost");
//        sf.setPort(2121);
//        sf.setUsername("anonymous");
//        sf.setPassword("");
//        return sf;
//    }
//
//    @Bean
//    public FtpInboundFileSynchronizer synchronizer() {
//
//        ftpInboundFileSynchronizer= new FtpInboundFileSynchronizer(sf());
//        ftpInboundFileSynchronizer.setDeleteRemoteFiles(false);
//        ftpInboundFileSynchronizer.setRemoteDirectory(REMOTE_DIRECTORIES.get(0)); // Set remote directory dynamically
//        ftpInboundFileSynchronizer.setFilter(new FtpSimplePatternFileListFilter("*.txt")); // Example filter
//        return ftpInboundFileSynchronizer;
//    }
//
//    @Bean
//    @InboundChannelAdapter(channel = "ftpZipChannel", poller = @Poller(fixedDelay = "5000"))
//    public MessageSource<?> ftpMessageSource() {
//        FtpInboundFileSynchronizingMessageSource source = new FtpInboundFileSynchronizingMessageSource(synchronizer());
//        File localDirectory = new File("/tmp/incoming");
//        source.setLocalDirectory(localDirectory);
//        source.setAutoCreateLocalDirectory(true);
//        return source;
//    }
//
//    @ServiceActivator(inputChannel = "ftpZipChannel")
//    public void handleIncomingFolder(File file) throws Exception {
//        logger.info("New File detected: {}", file.getName());
//        if (file.isDirectory()) {
//            logger.info("New folder detected: {}", file.getName());
//        }
//    }
//    @Scheduled(fixedRate = 4000) // 4000 milliseconds = 4 seconds
//    public void updateRemoteDirectoryPath() {
//
//        logger.info("triggered for updating file path");
////        String newDate = new SimpleDateFormat("yyyyMMdd").format(new Date()); // Get current date
////        String newDirectoryPath = "/" + newDate + "/Newsroom/Folder1"; // Construct new directory path
//
////        if (!newDirectoryPath.equals(remoteDirectoryPath)) {
////            logger.info("Updating remote directory from {} to {}", remoteDirectoryPath, newDirectoryPath);
////            remoteDirectoryPath = newDirectoryPath;
//
//            // Update the synchronizer with the new directory
//            //ftpInboundFileSynchronizer.setRemoteDirectory("/20250211/Newsroom/Folder5/.zips");
//
//    }
//
//}