package kh.edu.istad.common.persistence.filter;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.regex.Pattern;

public final class GenericMongoQueryBuilder {

    private GenericMongoQueryBuilder() {}

    public static Query build(List<FilterCondition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return new Query();
        }

        List<Criteria> criteria = conditions.stream()
                .filter(condition -> condition.value() != null)
                .map(GenericMongoQueryBuilder::toCriteria)
                .toList();

        if (criteria.isEmpty()) {
            return new Query();
        }

        return new Query(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
    }

    private static Criteria toCriteria(FilterCondition condition) {
        Criteria criteria = Criteria.where(condition.field());

        return switch (condition.operator()) {
            case EQUALS -> criteria.is(condition.value());
            case CONTAINS -> criteria.regex(Pattern.quote(condition.value().toString()), "i");
            case GREATER_THAN -> criteria.gt(condition.value());
            case LESS_THAN -> criteria.lt(condition.value());
            case IN -> {
                if (!(condition.value() instanceof List<?> values)) {
                    throw new IllegalArgumentException("IN operator requires a List value");
                }
                yield criteria.in(values);
            }
        };
    }
}