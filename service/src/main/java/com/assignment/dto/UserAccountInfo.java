package com.assignment.dto;

import java.util.Objects;

public class UserAccountInfo {
    private AccountInfo accountInfo;
    private UserInfo userInfo;

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccountInfo that = (UserAccountInfo) o;
        return accountInfo.equals(that.accountInfo) && userInfo.equals(that.userInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountInfo, userInfo);
    }

    @Override
    public String toString() {
        return "UserAccountInfo{" +
                "accountInfo=" + accountInfo +
                ", userInfo=" + userInfo +
                '}';
    }
}
