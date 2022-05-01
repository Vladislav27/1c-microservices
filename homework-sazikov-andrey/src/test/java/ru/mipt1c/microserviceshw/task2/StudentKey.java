package ru.mipt1c.microserviceshw.task2;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
public class StudentKey implements StorageKey, Comparable<StudentKey> {

  @Id
  private String uuid;

  private int groupId;
  private String name;

  public StudentKey(int groupId, String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    this.groupId = groupId;
    this.name = name;
    this.uuid = toString();
  }

  public int getGroupId() {
    return groupId;
  }

  public String getName() {
    return name;
  }

  @Override
  public int compareTo(StudentKey other) {
    if (other == null) {
      throw new IllegalArgumentException("Compare with null");
    }

    int compareGroupId = Integer.compare(this.groupId, other.groupId);

    if (compareGroupId != 0) {
      return compareGroupId;
    } else {
      return this.name.compareTo(other.name);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StudentKey)) {
      return false;
    }

    StudentKey that = (StudentKey) o;

    if (getGroupId() != that.getGroupId()) {
      return false;
    }
    return getName().equals(that.getName());

  }

  @Override
  public int hashCode() {
    int result = getGroupId();
    result = 31 * result + getName().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "g" + getGroupId() + "-" + getName();
  }

  @Override
  public String getUUID() {
    return uuid;
  }

}