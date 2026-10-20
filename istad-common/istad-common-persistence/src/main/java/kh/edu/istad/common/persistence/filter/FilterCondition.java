package kh.edu.istad.common.persistence.filter;

public record FilterCondition(String field, FilterOperator operator, Object value) {}