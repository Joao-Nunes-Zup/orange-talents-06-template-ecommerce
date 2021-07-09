package com.ot6.mercadolivre.product;

import com.ot6.mercadolivre.category.CategoryRepository;
import com.ot6.mercadolivre.product.dtos.NewProductRequest;
import com.ot6.mercadolivre.product.validation.ForbidRepeatedFeatures;
import com.ot6.mercadolivre.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ForbidRepeatedFeatures forbidRepeatedFeatures;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(forbidRepeatedFeatures);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<NewProductResponse> create(@RequestBody @Valid NewProductRequest newProductRequest) {
        Product product = newProductRequest.toEntity(categoryRepository, userRepository);
        productRepository.save(product);
        return ResponseEntity.ok(product.toNewProductResponse());
    }
}
