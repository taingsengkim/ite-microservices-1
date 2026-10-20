
package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.platform.customer.domain.dto.CustomerCountBySegment;
import kh.edu.istad.platform.customer.domain.dto.CustomerCountByStatus;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import kh.edu.istad.platform.customer.domain.port.out.CustomerStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class CustomerStatisticsReactiveRepositoryAdapter implements CustomerStatisticsRepository {

    private static final String CUSTOMER_COLLECTION = "customers";
    private static final String SEGMENT_COLLECTION = "customer_segments";

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<CustomerStatistics> getStatistics() {
        Mono<Long> totalCustomers = mongoTemplate.count(new Query(), CUSTOMER_COLLECTION);
        return Mono.zip(totalCustomers, getCountByStatus(), getCountBySegment())
                .map(result -> new CustomerStatistics(result.getT1(), result.getT2(), result.getT3()));
    }

    private Mono<List<CustomerCountByStatus>> getCountByStatus() {
        Aggregation aggregation = newAggregation(
                group("customerStatus").count().as("total"),
                project("total").and("_id").as("status")
        );
        return mongoTemplate.aggregate(aggregation, CUSTOMER_COLLECTION, Document.class)
                .map(doc -> new CustomerCountByStatus(doc.getString("status"), doc.get("total", Number.class).longValue()))
                .collectList();
    }

    private Mono<List<CustomerCountBySegment>> getCountBySegment() {
        return mongoTemplate.aggregate(buildSegmentAggregation(), CUSTOMER_COLLECTION, Document.class)
                .map(doc -> new CustomerCountBySegment(doc.getString("segmentType"), doc.get("total", Number.class).longValue()))
                .collectList();
    }

    private Aggregation buildSegmentAggregation() {
        Document segmentId = new Document("$getField", new Document("field", new Document("$literal", "$id")).append("input", "$customerSegmentDocument"));
        Document lookup = new Document("$lookup", new Document("from", SEGMENT_COLLECTION)
                .append("let", new Document("segmentId", segmentId))
                .append("pipeline", List.of(new Document("$match", new Document("$expr", new Document("$eq", List.of("$_id", "$$segmentId"))))))
                .append("as", "segment"));
        Document unwind = new Document("$unwind", new Document("path", "$segment").append("preserveNullAndEmptyArrays", true));
        Document group = new Document("$group", new Document("_id", new Document("$ifNull", List.of("$segment.type", "No segment")))
                .append("total", new Document("$sum", 1)));
        Document project = new Document("$project", new Document("_id", 0).append("segmentType", "$_id").append("total", 1));

        return newAggregation(Aggregation.stage(lookup), Aggregation.stage(unwind), Aggregation.stage(group), Aggregation.stage(project));
    }
}
