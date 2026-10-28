package kh.edu.istad.platform.order;

import kh.edu.istad.platform.order.persistence.repository.BusinessReactiveRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootTest
@EnableR2dbcRepositories
class BusinessTests {

    private static final Logger log = LoggerFactory.getLogger(BusinessTests.class);
    @Autowired
    private BusinessReactiveRepository businessReactiveRepository;

    @Test
    void testFindBusiness() {
        var businesses = businessReactiveRepository
                .findAll()
                .collectList()
                .block();
        log.info(businesses.toString());
    }
}
