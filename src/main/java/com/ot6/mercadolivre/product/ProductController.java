package com.ot6.mercadolivre.product;

import com.ot6.mercadolivre.category.CategoryRepository;
import com.ot6.mercadolivre.product.dtos.*;
import com.ot6.mercadolivre.product.validation.ForbidRepeatedFeatures;
import com.ot6.mercadolivre.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    OpinionRepository opinionRepository;

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
    public ResponseEntity<NewProductResponse> createNewProduct(@RequestBody @Valid NewProductRequest newProductRequest) {
        Product product = newProductRequest.toEntity(categoryRepository, userRepository);
        productRepository.save(product);
        return ResponseEntity.ok(product.toNewProductResponse());
    }

    @Transactional
    @PostMapping("/{id}/images")
    public ResponseEntity<?> addProductImages(@PathVariable Long id, @Valid NewProductImageRequest imageRequest) {
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

    @Transactional
    @PostMapping("/{productId}/opinions")
    public ResponseEntity<?> addProductOpinion(
            @PathVariable Long productId,
            @RequestBody @Valid NewOpinionRequest opinionRequest)
    {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            return ResponseEntity.badRequest().body("Produto não cadastrado");
        }

        Opinion opinion = opinionRequest.toEntity(product.get(), userRepository);
        opinionRepository.save(opinion);

        return ResponseEntity.ok(opinion.toNewOpinionResponse());
    }
}
