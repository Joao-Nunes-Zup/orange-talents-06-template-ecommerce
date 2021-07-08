package com.ot6.mercadolivre.category;

import com.ot6.mercadolivre.category.dtos.NewCategoryResponse;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @ManyToOne
    private Category motherCategory;

    @Deprecated
    public Category() {}

    public Category(@NotBlank String name) {
        this.name = name;
    }

    public void setMother(Category motherCategory) {
        this.motherCategory = motherCategory;
    }

    public NewCategoryResponse toNewCategoryResponse() {
        return new NewCategoryResponse(this.id);
    }
}
