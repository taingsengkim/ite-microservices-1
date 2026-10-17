package kh.edu.istad.platform.business.domain.valueobject;


import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;

public record BusinessName(String legalName, String displayName) {

    public BusinessName {
        if (legalName == null || legalName.isBlank()) {
            throw new BusinessDomainException("Legal name must not be empty");
        }
        legalName = legalName.trim();
        if (displayName == null || displayName.isBlank()) {
            displayName = legalName;
        } else {
            displayName = displayName.trim();
        }
    }

    public BusinessName(String name) {
        this(name, name);
    }

    public String getPrimaryName() {
        return displayName != null ? displayName : legalName;
    }
}
