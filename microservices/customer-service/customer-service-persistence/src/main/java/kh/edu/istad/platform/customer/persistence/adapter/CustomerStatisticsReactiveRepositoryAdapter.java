package kh.edu.istad.platform.customer.client.persistence.adapter;

import kh.edu.istad.platform.customer.client.domain.dto.CustomerCountBySegment;
import kh.edu.istad.platform.customer.client.domain.dto.CustomerCountByStatus;
import kh.edu.istad.platform.customer.client.domain.dto.CustomerStatistics;
import kh.edu.istad.platform.customer.client.domain.port.out.CustomerStatisticsRepository;
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

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<CustomerStatistics> getStatistics() {

        Aggregation statusAggregation = newAggregation(
                group("customerStatus").count().as("total"),
                project("total").and("_id").as("status")
        );

        Mono<List<CustomerCountByStatus>> byStatus =
                mongoTemplate.aggregate(statusAggregation, "customers", Document.class)
                        .map(doc -> new CustomerCountByStatus(doc.getString("status"), doc.get("total", Number.class).longValue()))
                        .collectList();
        Aggregation segmentAggregation = newAggregation(
                Aggregation.stage(new Document("$lookup",
                        new Document("from", "customer_segments")
                                .append("let", new Document("segmentId", new Document("$getField", new Document("field", new Document("$literal", "$id")).append("input", "$customerSegmentDocument"))))
                                .append("pipeline", List.of(new Document("$match", new Document("$expr", new Document("$eq", List.of("$_id", "$$segmentId"))))))
                                .append("as", "segment")
                )),
                Aggregation.stage(new Document("$unwind", new Document("path", "$segment").append("preserveNullAndEmptyArrays", true))),
                Aggregation.stage(new Document("$group", new Document("_id", new Document("$ifNull", List.of("$segment.type", "No segment"))).append("total", new Document("$sum", 1)))),
                Aggregation.stage(new Document("$project", new Document("_id", 0).append("segmentType", "$_id").append("total", 1)))
        );

        Mono<List<CustomerCountBySegment>> bySegment =
                mongoTemplate.aggregate(segmentAggregation, "customers", Document.class)
                        .map(doc -> new CustomerCountBySegment(doc.getString("segmentType"), doc.get("total", Number.class).longValue()))
                        .collectList();

        return Mono.zip(mongoTemplate.count(new Query(), "customers"), byStatus, bySegment)
                .map(result -> new CustomerStatistics(result.getT1(), result.getT2(), result.getT3()));
    }
}