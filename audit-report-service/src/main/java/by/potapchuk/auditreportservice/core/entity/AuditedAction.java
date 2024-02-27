package by.potapchuk.auditreportservice.core.entity;

public enum AuditedAction {

    REGISTRATION("пройден процесс регистрации"),
    VERIFICATION("пройден процесс верификации"),
    LOGIN("пройден процесс авторизации"),
    INFO_ABOUT_ME("запрошена информация о себе"),
    UPDATE_PASSWORD("пользователь обновил пароль"),
    INFO_ABOUT_ALL_USERS("запрошена информация о всех пользователях"),
    INFO_ABOUT_USER_BY_ID("запрошена информация о юзере по индентификатору"),
    CREATE_USER("создан юзер"),
    UPDATE_USER("юзер обновлен"),
    INFO_ABOUT_ALL_AUDITS("запрошена информация о всех аудитах"),
    INFO_ABOUT_AUDIT_BY_ID("запрошена информация об аудите по индентификатору"),
    INFO_ABOUT_ALL_REPORTS("запрошена информация о всех отчетах"),
    CREATE_REPORT("создан отчет"),
    INFO_ABOUT_ACCESS_REPORT("запрошена информация о доступности скачивания отчета"),
    SAVE_REPORT("скачивание отчета");

    private String description;

    AuditedAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
