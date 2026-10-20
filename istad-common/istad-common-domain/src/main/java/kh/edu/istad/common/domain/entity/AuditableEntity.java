package kh.edu.istad.common.domain.entity;

public class AuditableEntity<ID> extends BaseEntity<ID>{

    private String modifiedBy;
    private String modifiedAt;
    private String createdBy;
    private String createdAt;

    public String getModifiedBy() {
        return modifiedBy;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
