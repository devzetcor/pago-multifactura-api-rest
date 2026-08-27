//package com.davivienda.sv.app;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.TaskDecorator;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.lang.reflect.Method;
//import java.util.Map;
//import java.util.concurrent.Executor;
//
//@Configuration
//@EnableAsync
//public class AsyncConfig implements AsyncConfigurer {
//
//    private static final Logger LOGGER = LogManager.getLogger(AsyncConfig.class);
//
//    @Override
//    @Bean(name = "taskExecutor")
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(20);
//        executor.setMaxPoolSize(80);
//        executor.setQueueCapacity(200);
//        executor.setThreadNamePrefix("AsyncTask-");
//
//        // Decorador protegido contra nulos y excepciones
//        executor.setTaskDecorator(new SafeMdcTaskDecorator());
//
//        executor.initialize();
//        return executor;
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return (Throwable ex, Method method, Object... params) -> {
//            LOGGER.error("🛑 EXCEPCIÓN ASÍNCRONA NO CAPTURADA en método: " + method.getName(), ex);
//        };
//    }
//
//    /**
//     * Decorador seguro que no rompe el hilo si Log4j falla o el contexto es nulo.
//     */
//    public static class SafeMdcTaskDecorator implements TaskDecorator {
//        @Override
//        public Runnable decorate(Runnable runnable) {
//            // Capturamos el contexto en el hilo padre de forma segura
//            Map<?, ?> contextMap = null;
//            try {
//                contextMap = MDC.getContext();
//            } catch (Exception e) {
//                // Ignoramos error al obtener contexto para no detener el proceso
//            }
//
//            final Map<?, ?> finalContextMap = contextMap;
//
//            return () -> {
//                try {
//                    // Restauramos contexto en el hilo hijo
//                    if (finalContextMap != null && !finalContextMap.isEmpty()) {
//                        // En Log4j 1.x getContext devuelve Hashtable, usamos putAll seguro
//                        for (Map.Entry<?, ?> entry : finalContextMap.entrySet()) {
//                            if (entry.getKey() != null && entry.getValue() != null) {
//                                MDC.put(entry.getKey().toString(), entry.getValue());
//                            }
//                        }
//                    }
//                    runnable.run();
//                } finally {
//                    MDC.clear();
//                }
//            };
//        }
//    }
//}