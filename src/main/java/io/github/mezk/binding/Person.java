package io.github.mezk.binding;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Person {
    public UUID personId;
    public String name;
    public Integer age;
    public Document document;
}
