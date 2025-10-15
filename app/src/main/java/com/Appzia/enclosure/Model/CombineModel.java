package com.Appzia.enclosure.Model;

public class CombineModel {
    private get_users_all_contactChildModel userData;
    private get_users_all_contactChild2Model inviteData;

    public CombineModel(get_users_all_contactChildModel userData, get_users_all_contactChild2Model inviteData) {
        this.userData = userData;
        this.inviteData = inviteData;
    }

    public get_users_all_contactChildModel getUserData() {
        return userData;
    }

    public void setUserData(get_users_all_contactChildModel userData) {
        this.userData = userData;
    }

    public get_users_all_contactChild2Model getInviteData() {
        return inviteData;
    }

    public void setInviteData(get_users_all_contactChild2Model inviteData) {
        this.inviteData = inviteData;
    }
}
