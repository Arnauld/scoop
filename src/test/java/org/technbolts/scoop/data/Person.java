package org.technbolts.scoop.data;

public class Person {
    private String firstName, lastName;

    public Person() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return getFullName("%1$s %2$s");
    }

    public String getFullName(String format) {
        return String.format(format, getFirstName(), getLastName());
    }
    
    public void appendFullName(String format, StringBuilder dst) {
        dst.append(getFullName(format));
    }

}
