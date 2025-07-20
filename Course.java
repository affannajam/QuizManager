package main;

import java.io.Serializable;

/**
 * Course class to represent course information
 */
public class Course implements Serializable {
    private String code;
    private String name;
    private String type; // Theory or Lab

    public Course(String code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return code.equals(course.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + type + ")";
    }
}