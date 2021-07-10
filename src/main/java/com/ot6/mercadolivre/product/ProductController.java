package com.ot6.mercadolivre.product;

import com.ot6.mercadolivre.category.CategoryRepository;
import com.ot6.mercadolivre.product.dtos.NewProductImageRequest;
import com.ot6.mercadolivre.product.dtos.NewProductRequest;
import com.ot6.mercadolivre.product.dtos.NewProductResponse;
import com.ot6.mercadolivre.product.dtos.ProductWithImageResponse;
import com.ot6.mercadolivre.product.validation.ForbidRepeatedFeatures;
import com.ot6.mercadolivre.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @PersistenceContext
    EntityManager manager;

    @Autowired
    ForbidRepeatedFeatures forbidRepeatedFeatures;

    @Autowired
    private FakeUploader fakeUploader;

    @InitBinder(value = "newProductRequest")
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

    @Transactional
    @PostMapping("/{id}/images")
    public ResponseEntity<?> create(@PathVariable Long id, @Valid NewProductImageRequest imageRequest) {
        Optional<Product> possibleProduct = productRepository.findById(id);

        if (possibleProduct.isEmpty()) {
            return ResponseEntity.badRequest().body("Produto não cadastrado");
        }

        Product product = possibleProduct.get();

        if (!product.belongsToLoggedUser(userRepository)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Set<String> links = fakeUploader.send(imageRequest.getImages());
        product.addImages(links);
        productRepository.save(product);
        return ResponseEntity.ok(product.toNewProductWithImageResponse());
    }
}
