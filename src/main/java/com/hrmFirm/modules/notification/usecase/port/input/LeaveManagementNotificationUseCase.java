package com.hrmFirm.modules.notification.usecase.port.input;

public interface LeaveManagementNotificationUseCase {
    void sendLeaveApplicationConfirmationEmail();
    void sendLeaveApprovalNotificationEmail();
    void sendLeaveApprovalRequestEmail();
}
