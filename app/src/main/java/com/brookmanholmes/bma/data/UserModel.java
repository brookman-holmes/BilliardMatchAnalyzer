package com.brookmanholmes.bma.data;

/**
 * Created by Brookman Holmes on 2/19/2017.
 */

public class UserModel implements Comparable<UserModel> {
    public String name;
    public String id;
    public String imageUrl;

    public UserModel() {

    }

    public UserModel(String name) {
        this.name = name;
        this.id = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserModel userModel = (UserModel) o;

        if (!name.equals(userModel.name)) return false;
        if (!id.equals(userModel.id)) return false;
        return imageUrl != null ? imageUrl.equals(userModel.imageUrl) : userModel.imageUrl == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public int compareTo(UserModel o) {
        return name.compareTo(o.name);
    }
}
