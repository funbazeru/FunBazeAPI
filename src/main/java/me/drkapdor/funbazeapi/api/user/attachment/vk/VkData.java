package me.drkapdor.funbazeapi.api.user.attachment.vk;

public class VkData {

    private int userId;
    private String accessToken;
    private String email;
    private boolean hasJoinedGroupBefore;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isHasJoinedGroupBefore() {
        return hasJoinedGroupBefore;
    }

    public void setHasJoinedGroupBefore(boolean hasJoinedGroupBefore) {
        this.hasJoinedGroupBefore = hasJoinedGroupBefore;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean hasJoinedGroupBefore() {
        return hasJoinedGroupBefore;
    }

    public void setJoinedGroupBefore(boolean hasJoinedGroupBefore) {
        this.hasJoinedGroupBefore = hasJoinedGroupBefore;
    }
}
