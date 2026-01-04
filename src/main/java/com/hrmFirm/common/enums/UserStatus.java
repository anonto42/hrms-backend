package com.hrmFirm.common.enums;

import lombok.Getter;

@Getter
public enum UserStatus {

    // ACTIVE WORKING STATUSES
    ACTIVE("ACTIVE", "Active", "A", true, StatusCategory.ACTIVE),
    PROBATION("PROBATION", "Probation", "P", true, StatusCategory.ACTIVE),
    INTERN("INTERN", "Intern", "I", true, StatusCategory.ACTIVE),
    CONTRACTOR("CONTRACTOR", "Contractor", "C", true, StatusCategory.ACTIVE),
    PART_TIME("PART_TIME", "Part-Time", "PT", true, StatusCategory.ACTIVE),
    SECONDED("SECONDED", "Seconded", "S", true, StatusCategory.ACTIVE),
    REMOTE("REMOTE", "Remote Worker", "R", true, StatusCategory.ACTIVE),

    // LEAVE STATUSES
    ON_LEAVE("ON_LEAVE", "On Leave", "OL", false, StatusCategory.LEAVE),
    MATERNITY_LEAVE("MATERNITY_LEAVE", "Maternity Leave", "ML", false, StatusCategory.LEAVE),
    PATERNITY_LEAVE("PATERNITY_LEAVE", "Paternity Leave", "PL", false, StatusCategory.LEAVE),
    SICK_LEAVE("SICK_LEAVE", "Sick Leave", "SL", false, StatusCategory.LEAVE),
    STUDY_LEAVE("STUDY_LEAVE", "Study Leave", "STL", false, StatusCategory.LEAVE),
    UNPAID_LEAVE("UNPAID_LEAVE", "Unpaid Leave", "UL", false, StatusCategory.LEAVE),
    MEDICAL_LEAVE("MEDICAL_LEAVE", "Medical Leave", "MDL", false, StatusCategory.LEAVE),
    ANNUAL_LEAVE("ANNUAL_LEAVE", "Annual Leave", "AL", false, StatusCategory.LEAVE),

    // SUSPENDED STATUSES
    SUSPENDED("SUSPENDED", "Suspended", "SUS", false, StatusCategory.SUSPENDED),
    INVESTIGATION("INVESTIGATION", "Under Investigation", "INV", false, StatusCategory.SUSPENDED),
    DISCIPLINARY("DISCIPLINARY", "Disciplinary Action", "DISC", false, StatusCategory.SUSPENDED),

    // TERMINATION STATUSES
    RESIGNED("RESIGNED", "Resigned", "RES", false, StatusCategory.TERMINATED),
    TERMINATED("TERMINATED", "Terminated", "TER", false, StatusCategory.TERMINATED),
    RETIRED("RETIRED", "Retired", "RET", false, StatusCategory.TERMINATED),
    DECEASED("DECEASED", "Deceased", "DEC", false, StatusCategory.TERMINATED),
    REDUNDANT("REDUNDANT", "Made Redundant", "RED", false, StatusCategory.TERMINATED),

    // PRE-EMPLOYMENT STATUSES
    PENDING("PENDING", "Pending", "PEND", false, StatusCategory.PENDING),
    OFFER_SENT("OFFER_SENT", "Offer Sent", "OFFER", false, StatusCategory.PENDING),
    OFFER_ACCEPTED("OFFER_ACCEPTED", "Offer Accepted", "OACC", false, StatusCategory.PENDING),
    BACKGROUND_CHECK("BACKGROUND_CHECK", "Background Check", "BGC", false, StatusCategory.PENDING),
    PRE_ONBOARDING("PRE_ONBOARDING", "Pre-Onboarding", "PREO", false, StatusCategory.PENDING),

    // SYSTEM STATUSES
    INVITED("INVITED", "Invited", "INV", false, StatusCategory.SYSTEM),
    INVITATION_EXPIRED("INVITATION_EXPIRED", "Invitation Expired", "INVE", false, StatusCategory.SYSTEM),
    LOCKED("LOCKED", "Account Locked", "LOCK", false, StatusCategory.SYSTEM),
    PASSWORD_EXPIRED("PASSWORD_EXPIRED", "Password Expired", "PWE", false, StatusCategory.SYSTEM),
    PENDING_APPROVAL("PENDING_APPROVAL", "Pending Approval", "PAPP", false, StatusCategory.SYSTEM),
    INACTIVE("INACTIVE", "Inactive", "INACT", false, StatusCategory.SYSTEM),

    // ARCHIVAL STATUSES
    ARCHIVED("ARCHIVED", "Archived", "ARCH", false, StatusCategory.ARCHIVED),
    FORMER_EMPLOYEE("FORMER_EMPLOYEE", "Former Employee", "FEMP", false, StatusCategory.ARCHIVED),
    HISTORICAL("HISTORICAL", "Historical Record", "HIST", false, StatusCategory.ARCHIVED);

    private final String code;
    private final String displayName;
    private final String shortCode;
    private final boolean canAccessSystem;
    private final StatusCategory category;

    UserStatus(String code, String displayName, String shortCode,
               boolean canAccessSystem, StatusCategory category) {
        this.code = code;
        this.displayName = displayName;
        this.shortCode = shortCode;
        this.canAccessSystem = canAccessSystem;
        this.category = category;
    }

    // Business logic methods
    public boolean isEligibleForBenefits() {
        return this.category == StatusCategory.ACTIVE ||
                this == PROBATION ||
                this == PART_TIME;
    }

    public boolean isPayrollEligible() {
        return this.category == StatusCategory.ACTIVE ||
                this.category == StatusCategory.LEAVE ||
                this == PROBATION ||
                this == PART_TIME;
    }

    public boolean requiresManagerApproval() {
        return this == ON_LEAVE ||
                this == STUDY_LEAVE ||
                this == UNPAID_LEAVE ||
                this == SECONDED;
    }
}