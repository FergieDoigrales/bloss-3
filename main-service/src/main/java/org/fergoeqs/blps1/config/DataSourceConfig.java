//package org.fergoeqs.blps1.config;
//
//import com.atomikos.jdbc.AtomikosDataSourceBean;
//import org.postgresql.xa.PGXADataSource;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//
//@Configuration
//public class DataSourceConfig {
//
//    private PGXADataSource createXaDataSource() {
//        PGXADataSource ds = new PGXADataSource();
//        ds.setUrl("jdbc:postgresql://localhost:5432/blpsdb");
//        ds.setUser("blps");
//        ds.setPassword("blps");
//        return ds;
//    }
//
//    @Bean(name = "sharedDataSource")
//    @Primary
//    public DataSource sharedDataSource() {
//        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
//        xaDataSource.setXaDataSource(createXaDataSource());
//        xaDataSource.setUniqueResourceName("sharedDS");
//        xaDataSource.setPoolSize(5);
//        xaDataSource.setMaxLifetime(1800); // Устанавливаем время жизни соединения (в секундах)
//        xaDataSource.setMaxIdleTime(300); // Максимальное время простоя соединения
//        return xaDataSource;
//    }
//}