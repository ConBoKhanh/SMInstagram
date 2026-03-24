package com.example.sminstagram.bases;
import com.example.sminstagram.entities.FilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BaseSpecification {
    public static <T> Specification<T> buildFilter(List<FilterRequest> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters == null || filters.isEmpty()) {
                return cb.conjunction(); // không filter gì = lấy tất cả
            }

            for (FilterRequest filter : filters) {

                // Xử lý sort
                if (filter.getSort() != null && filter.getField() != null) {
                    if ("desc".equalsIgnoreCase(filter.getSort())) {
                        query.orderBy(cb.desc(root.get(filter.getField())));
                    } else {
                        query.orderBy(cb.asc(root.get(filter.getField())));
                    }
                    continue; // sort thì không thêm vào predicate
                }

                // Bỏ qua nếu thiếu field hoặc value
                if (filter.getField() == null || filter.getValue() == null
                        || filter.getOperator() == null) continue;

                switch (filter.getOperator().toUpperCase()) {
                    case "LIKE" -> predicates.add(
                            cb.like(cb.lower(root.get(filter.getField())),
                                    "%" + filter.getValue().toLowerCase() + "%")
                    );
                    case "EQUAL" -> predicates.add(
                            cb.equal(root.get(filter.getField()), filter.getValue())
                    );
                    case "NOT_EQUAL" -> predicates.add(
                            cb.notEqual(root.get(filter.getField()), filter.getValue())
                    );
                    case "GT" -> predicates.add(
                            cb.greaterThan(root.get(filter.getField()), filter.getValue())
                    );
                    case "LT" -> predicates.add(
                            cb.lessThan(root.get(filter.getField()), filter.getValue())
                    );
                    case "GT_OR_EQUAL" -> predicates.add(
                            cb.greaterThanOrEqualTo(root.get(filter.getField()), filter.getValue())
                    );
                    case "LT_OR_EQUAL" -> predicates.add(
                            cb.lessThanOrEqualTo(root.get(filter.getField()), filter.getValue())
                    );
                    case "IS_NULL" -> predicates.add(
                            cb.isNull(root.get(filter.getField()))
                    );
                    case "IS_NOT_NULL" -> predicates.add(
                            cb.isNotNull(root.get(filter.getField()))
                    );
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
