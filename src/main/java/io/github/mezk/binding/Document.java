package io.github.mezk.binding;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Document {
    public UUID documentId;
    public UUID personId;
    public String code;
}
