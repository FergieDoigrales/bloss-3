    package org.fergoeqs.blps1.config;

    import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
    import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.transaction.PlatformTransactionManager;

    import javax.sql.DataSource;
    @Configuration
    public class CamundaConfig {

        @Bean
        public SpringProcessEngineConfiguration processEngineConfiguration(
                DataSource dataSource,
                PlatformTransactionManager transactionManager) {

            SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
            config.setDataSource(dataSource);
            config.setTransactionManager(transactionManager);
            config.setDatabaseSchemaUpdate("true");
            config.setJobExecutorActivate(true);
            config.setHistory("full");

            return config;
        }

        @Bean
        public ProcessEngineFactoryBean processEngine(
                SpringProcessEngineConfiguration processEngineConfiguration) {

            ProcessEngineFactoryBean factory = new ProcessEngineFactoryBean();
            factory.setProcessEngineConfiguration(processEngineConfiguration);
            return factory;
        }
    }