package com.ot6.mercadolivre.shared.email;

import com.ot6.mercadolivre.product.ProductQuestion;
import com.ot6.mercadolivre.sale.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class Emails {

    @Autowired
    Mailer mailer;

    public void newQuestion(ProductQuestion question) {

        mailer.send(
                "<html>....</html>",
                "Question to be answered",
                "question@mercadolivre.com",
                question.getUserEmail(),
                question.getProductOwnerEmail()
        );
    }

    public void newSale(Sale sale) {

        mailer.send(
                "<html>....</html>",
                "New purchase",
                "purchase@mercadolivre.com",
                sale.getUserEmail(),
                sale.getProductOwnerEmail()
        );
    }
}
