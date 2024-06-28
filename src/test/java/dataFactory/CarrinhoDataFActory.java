package dataFactory;

import com.github.javafaker.Faker;
import pojo.CarrinhoPojo;
import pojo.ProdutoPojo;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoDataFActory {

    private static Faker faker = new Faker();

    public static String criarCarrinho() {
        int quantidade = faker.number().numberBetween(1, 5);
        String produtoId = faker.number().digits(24);

        return "{\n" +
                "  \"produtos\": [\n" +
                "    {\n" +
                "      \"idProduto\": \"" + produtoId + "\",\n" +
                "      \"quantidade\": " + quantidade + "\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }





}
