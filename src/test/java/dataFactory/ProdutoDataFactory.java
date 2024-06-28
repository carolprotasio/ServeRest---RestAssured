package dataFactory;

import pojo.ProdutoPojo;
import com.github.javafaker.Faker;

public class ProdutoDataFactory {
    private static final Faker faker = new Faker();

    public static ProdutoPojo registerNewProduct() {
        ProdutoPojo newProduct = new ProdutoPojo();
        newProduct.setNome(faker.commerce().productName());
        newProduct.setPreco(faker.number().numberBetween(5, 5000));
        newProduct.setDescricao(faker.commerce().material());
        newProduct.setQuantidade(faker.number().numberBetween(1, 100));
        return newProduct;
    }
    public static ProdutoPojo productEdit() {
        ProdutoPojo newProduct = new ProdutoPojo();
        newProduct.setNome(faker.commerce().productName());
        newProduct.setPreco(faker.number().numberBetween(5, 5000));
        newProduct.setDescricao(faker.commerce().material());
        newProduct.setQuantidade(faker.number().numberBetween(1, 100));
        return newProduct;
    }


}
