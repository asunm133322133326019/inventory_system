package com.example;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController 
{
	@Autowired
	ProductJpaRepository productJpaRepository;
	
	@PostMapping
    public String insertProduct(@RequestBody Product product){

        productJpaRepository.save(product);

        return "Product added successfully";
    }
	
	@PostMapping("/list")
    public String insertProducts(@RequestBody List<Product> products){
        productJpaRepository.saveAll(products);

        return "Products added successfully";
    }
	
	@GetMapping("/list")
    public List<Product> getAllProduct(){
        return productJpaRepository.findAll();
    }

	@GetMapping("/{id}")
    public Product getProductById(@PathVariable int id){
      Optional<Product> optional =productJpaRepository.findById(id);

      if(optional.isPresent()) {
         return  optional.get();
      }
       throw new RuntimeException("Product not found");
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable int id){

        Product product=getProductById(id);

        productJpaRepository.delete(product);
       return "Product deleted successfully";
    }

    @GetMapping("/list/sort/{column}")
    public List<Product> sortByColumn(@PathVariable String column){

       return productJpaRepository.findAll(Sort.by(column));
    }

    @DeleteMapping("/delete/{pk}")
    public String deleteProductByPrimaryKey(@PathVariable int pk){

        productJpaRepository.deleteById(pk);
        return "Product Deleted successfully";
    }

    @DeleteMapping("/delete-all")
    public String clearTable(){
        productJpaRepository.deleteAll();
        return "All products deleted successfully";
    }


}
