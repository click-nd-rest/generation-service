package com.github.click.nd.rest.generation.service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.click.nd.rest.generation.service.util.CaseUtil;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public final class ResourceField {

    private final String name;
    private final DataType type;

    /**
     * Methods used in .mustache templates
     */
    public String getNameUpperCamel() {
        return CaseUtil.toUpperCamel(name);
    }

    public String getNameLowerCamel() {
        return CaseUtil.toLowerCamel(name);
    }

    /**
     * Lombok's implementation. Copy it because we can't copy hashCode() and generate equals either
     */
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof final ResourceField other)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (!Objects.equals(this$name, other$name)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        return Objects.equals(this$type, other$type);
    }

    /**
     * Using lombok implementation but call persistentHashCode() method
     */
    @Override
    public int hashCode() {
        int prime = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * prime + ($name == null ? 43 : $name.hashCode());
        DataType type = this.getType();
        result = result * prime + (type == null ? 43 : type.persistentHashCode());
        return result;
    }
}
