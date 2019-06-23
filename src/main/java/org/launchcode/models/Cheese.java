package org.launchcode.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

/**
 * Created by LaunchCode
 */
@Entity
public class Cheese {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=15)
    private String name;

    @NotNull
    @Size(min=1, message = "Description must not be empty")
    private String description;

    @ManyToOne // there can be many cheeses for any one category
    private Category category;

    @ManyToMany(mappedBy = "cheeses") // there can be multiple cheese items related to
    // multiple menu items
    private List<Menu> menus;

    public Cheese(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Cheese() { }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory( ) {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass( ) != o.getClass( )) return false;
        Cheese cheese = (Cheese) o;
        return id == cheese.id;
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(id);
    }
}
