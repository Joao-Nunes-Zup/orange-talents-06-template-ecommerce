package com.ot6.mercadolivre.product;

import com.ot6.mercadolivre.category.Category;
import com.ot6.mercadolivre.product.dtos.NewProductFeatureRequest;
import com.ot6.mercadolivre.user.User;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private Set<ProductFeature> features = new HashSet<>();

    @NotBlank
    @Size(max = 1000)
    private String description;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    private LocalDateTime creationDate = LocalDateTime.now();

    public Product(
            String name,
            BigDecimal valor,
            Integer quantidade,
            List<NewProductFeatureRequest> featuresRequest,
            String description,
            Category category,
            User user
    ) {
        this.name = name;
        this.valor = valor;
        this.quantity = quantidade;
        this.description = description;
        this.category = category;
        this.user = user;

        Set<ProductFeature> newFeatures =
                featuresRequest.stream()
                        .map(featureRequest -> featureRequest.toEntity(this))
                        .collect(Collectors.toSet());

        this.features.addAll(newFeatures);
    }

    public NewProductResponse toNewProductResponse() {
        return new NewProductResponse(this.name);
    }
}
