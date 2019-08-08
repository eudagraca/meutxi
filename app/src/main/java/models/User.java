package models;

import com.parse.ParseFile;

public class User {
    private String name;
    private String email;
    private String password;
    private ParseFile image;

    public void setUid(String uid) {
    }

    public ParseFile getImage() {
        return image;
    }

    public void setImage(ParseFile image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
