package com.ot6.mercadolivre.sale;

import com.ot6.mercadolivre.product.Product;
import com.ot6.mercadolivre.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull
    @ManyToOne
    private Product product;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @ManyToOne
    private User buyer;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private PaymentGateway gateway;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Deprecated
    public Sale() {}

    public Sale(Product product, Integer quantity, User buyer, PaymentGateway gateway) {
        this.product = product;
        this.quantity = quantity;
        this.buyer= buyer;
        this.gateway = gateway;
        this.status = Status.INICIADA;
    }

    public Long getId() {
        return Id;
    }

    public String getUserEmail() {
        return this.buyer.getEmail();
    }

    public String getProductOwnerEmail() {
        return this.product.getUser().getEmail();
    }
}
