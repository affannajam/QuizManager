package main;

/**
 * Base class for system users (Teacher and Student)
 */
public abstract class User implements java.io.Serializable {
    protected String username;
    protected String password;
    protected String name;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getName() {
        return name;
    }
}