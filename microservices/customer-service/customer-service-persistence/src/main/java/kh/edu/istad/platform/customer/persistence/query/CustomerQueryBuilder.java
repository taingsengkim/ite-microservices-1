
package kh.edu.istad.platform.customer.persistence.query;

import kh.edu.istad.platform.customer.domain.dto.CustomerFilter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class CustomerQueryBuilder {

    private CustomerQueryBuilder() {}

    public static Query build(CustomerFilter filter) {
        List<Criteria> criteria = new ArrayList<>();

        addContains(criteria, "username", filter.username());
        addContains(criteria, "familyName", filter.familyName());
        addContains(criteria, "givenName", filter.givenName());
        addContains(criteria, "email", filter.email());
        addContains(criteria, "phoneNumber", filter.phoneNumber());
        addExactIgnoreCase(criteria, "customerStatus", filter.customerStatus());

        if (criteria.isEmpty()) {
            return new Query();
        }

        return new Query(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
    }

    private static void addContains(List<Criteria> criteria, String field, String value) {
        if (value != null && !value.isBlank()) {
            criteria.add(Criteria.where(field).regex(Pattern.quote(value.trim()), "i"));
        }
    }

    private static void addExactIgnoreCase(List<Criteria> criteria, String field, String value) {
        if (value != null && !value.isBlank()) {
            criteria.add(Criteria.where(field).regex("^" + Pattern.quote(value.trim()) + "$", "i"));
        }
    }
}
