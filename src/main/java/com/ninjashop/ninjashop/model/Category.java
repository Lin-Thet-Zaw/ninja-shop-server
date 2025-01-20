package com.ninjashop.ninjashop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    public Category(Category parent) {
        this.parent = parent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setName(@NotNull @Size(max = 50) String name) {
        this.name = name;
    }

    public @NotNull @Size(max = 50) String getName() {
        return name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Category(Long id, String name, Category parent, int level) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.level = level;
    }

    public Category() {
    }

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    private int level;

    public Category(Long id, Category parent, String name, int level) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.level = level;
    }
}
