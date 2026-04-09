package com.epam.springboot_rabbitmq_demo.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.remote.aop.RotatingServerAdvice;
import org.springframework.integration.file.remote.session.DelegatingSessionFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MultipleFTPConfigNewsRoomTest {

    @InjectMocks
    private MultipleFTPConfigNewsRoom multipleFTPConfigNewsRoom;

    @Test
    void testTaskExecutorNotNull() {
        TaskExecutor taskExecutor = multipleFTPConfigNewsRoom.taskExecutor();
        assertNotNull(taskExecutor);
    }

    @Test
    void testTaskExecutorIsThreadPoolType() {
        TaskExecutor taskExecutor = multipleFTPConfigNewsRoom.taskExecutor();
        assertInstanceOf(ThreadPoolTaskExecutor.class, taskExecutor);
    }

    @Test
    void testTaskExecutorCorePoolSize() {
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) multipleFTPConfigNewsRoom.taskExecutor();
        assertEquals(2, taskExecutor.getCorePoolSize());
    }

    @Test
    void testTaskExecutorMaxPoolSize() {
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) multipleFTPConfigNewsRoom.taskExecutor();
        assertEquals(5, taskExecutor.getMaxPoolSize());
    }

    @Test
    void testTaskExecutorThreadNamePrefix() {
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) multipleFTPConfigNewsRoom.taskExecutor();
        assertEquals("task-thread-", taskExecutor.getThreadNamePrefix());
    }

    @Test
    void testDelegatingSessionFactoryNotNull() {
        DelegatingSessionFactory<?> factory = multipleFTPConfigNewsRoom.delegatingSessionFactory();
        assertNotNull(factory);
    }

    @Test
    void testAdviceNotNull() {
        RotatingServerAdvice advice = multipleFTPConfigNewsRoom.advice();
        assertNotNull(advice);
    }

    @Test
    void testFlowNotNull() {
        IntegrationFlow flow = multipleFTPConfigNewsRoom.flow();
        assertNotNull(flow);
    }
}
