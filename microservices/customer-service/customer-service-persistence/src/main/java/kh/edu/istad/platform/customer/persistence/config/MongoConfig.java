package kh.edu.istad.platform.customer.client.persistence.config;


import kh.edu.istad.platform.customer.client.persistence.converter.CustomerIdReadConverter;
import kh.edu.istad.platform.customer.client.persistence.converter.CustomerIdWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
@EnableReactiveMongoAuditing(auditorAwareRef = "auditorAwareConfig")
public class MongoConfig {
    @Bean
    public MongoCustomConversions bean(){
        return MongoCustomConversions.create(
                adapter->{
                    adapter.registerConverter(new CustomerIdReadConverter());
                    adapter.registerConverter((new CustomerIdWriteConverter()));
                }
        );
    }
}
