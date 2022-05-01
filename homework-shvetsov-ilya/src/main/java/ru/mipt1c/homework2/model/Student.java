package ru.mipt1c.homework2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * Пример сложного объекта для хранения.
 */
@Entity
@ToString
@NoArgsConstructor
@Getter
public class Student extends AbstractValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int groupId;
    private String name;
    private String hometown;
    private Date birthDate;
    private boolean hasDormitory;
    private double averageScore;

    public Student(int groupId, String name, String hometown,
                   Date birthDate, boolean hasDormitory, double averageScore) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.groupId = groupId;
        this.name = name;
        this.hometown = hometown;
        this.birthDate = birthDate;
        this.hasDormitory = hasDormitory;
        this.averageScore = averageScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
//        if (!super.equals(o)) {
//            return false;
//        }

        Student student = (Student) o;

        if (isHasDormitory() != student.isHasDormitory()) {
            return false;
        }
        if (Double.compare(student.getAverageScore(), getAverageScore()) != 0) {
            return false;
        }
        if (getHometown() != null ? !getHometown().equals(student.getHometown()) : student.getHometown() != null) {
            return false;
        }
        return getBirthDate() != null ? getBirthDate().equals(student.getBirthDate()) : student.getBirthDate() == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (getHometown() != null ? getHometown().hashCode() : 0);
        result = 31 * result + (getBirthDate() != null ? getBirthDate().hashCode() : 0);
        result = 31 * result + (isHasDormitory() ? 1 : 0);
        temp = Double.doubleToLongBits(getAverageScore());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
