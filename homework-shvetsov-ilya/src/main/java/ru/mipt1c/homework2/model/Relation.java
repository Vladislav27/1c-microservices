package ru.mipt1c.homework2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Relation {
    @Id
    private String keyId;
    @Getter
    private int valueId;
}
