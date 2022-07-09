package me.drkapdor.funbazeapi.api.user.attachment.vk;

/**
 * Информация о привязанной к аккаунту
 * пользовательской страницы ВКонтакте.
 *
 * @author DrKapdor
 */

public class VkData {

    private int userId;
    private String accessToken;
    private String email;
    private boolean hasJoinedGroupBefore;

    /**
     * Возвращает идентификатор пользовательской страницы ВКонтакте
     * @return Идентификатор страницы ВКонтакте
     */

    public int getUserId() {
        return userId;
    }

    /**
     * Устанавливает идентификатор пользовательской страницы ВКонтакте.
     * @param userId Идентификатор страницы ВКонтакте
     */

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Возвращает access-token для доступка
     * к сведениям о странице ВКонтакте.
     *
     * @return Access-token
     */

    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Устанавливает access-token для доступка
     * к сведениям о странице ВКонтакте.
     *
     * @param accessToken Access-token
     */

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Проверяет, вступал ли пользователь в официально
     * сообщество проекта FunBaze во ВКонтакте ранее.
     *
     * @return Пользователь ранее вступал в сообщество
     */

    public boolean hasJoinedGroupBefore() {
        return hasJoinedGroupBefore;
    }

    /**
     * Обозначает, вступал ли пользователь в официальное
     * сообщество проекта FunBaze во ВКонтакте ранее.
     *
     * @param hasJoinedGroupBefore Пользователь ранее вступал в сообщество
     */

    public void setHasJoinedGroupBefore(boolean hasJoinedGroupBefore) {
        this.hasJoinedGroupBefore = hasJoinedGroupBefore;
    }

    /**
     * Возвращает адрес электронного почтового ящика
     * @return Адрес электронного почтового ящика
     */

    public String getEmail() {
        return email;
    }

    /**
     * Устанавливает адрес электронного почтового ящика
     * @param  email Адрес электронного почтового ящика
     */

    public void setEmail(String email) {
        this.email = email;
    }
}
