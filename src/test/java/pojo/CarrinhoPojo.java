package pojo;

import java.util.List;

public class CarrinhoPojo {
    private String idProduto;
    private int quantidade;
    private List<ProdutoPojo> produtos;

    public List<ProdutoPojo> getProdutos() {
        return produtos;
    }
    public void setProdutos(List<ProdutoPojo> produtos) {
        this.produtos = produtos;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
