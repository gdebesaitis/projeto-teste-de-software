package dal;

import model.POSController;
import model.dto.ProductDTO;
import model.dto.Response;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Teste do Requisito Principal (Fluxo Principal / Cenário Principal) - Gabriel Custodio
 * Objetivo: Validar o fluxo completo de venda e baixa de estoque.
 * * NOTA: Este teste é PROJETADO PARA FALHAR na validação final de estoque
 * (Cenário de Regressão).
 */
public class SystemCriticalScenarioTest {

    private POSController controller;
    private ProductDTO produtoTeste;

    @Before
    public void setUp() {
        controller = new POSController();
        
        // ESTRATÉGIA ANTI-CONFLITO:
        // Geramos um código de barras único baseado no tempo para evitar erro de duplicidade
        String uniqueBarcode = "999-" + System.currentTimeMillis();
        
        produtoTeste = new ProductDTO();
        produtoTeste.setProductName("Produto Teste Fluxo");
        produtoTeste.setBarcode(uniqueBarcode); // Barcode único
        produtoTeste.setPrice(10.0);
        produtoTeste.setStockQuantity(10.0); // Estoque Inicial: 10
        produtoTeste.setCategoryId(1);
        produtoTeste.setSupplierId(1);
        produtoTeste.setQuantityType("counted");
    }

    @Test
    public void testCenarioVendaCompletaBaixaEstoque() {
        System.out.println("Iniciando Teste do Cenário Principal...");

        // Passo 1: Cadastrar Produto
        Response resCad = controller.addProduct(produtoTeste);
        
        // MELHORIA DE DEBUG: Se falhar, mostra o motivo exato do banco de dados
        assertTrue("Falha ao cadastrar produto: " + resCad.getErrorMessages(), resCad.isSuccessfull());
        System.out.println("- Produto cadastrado com sucesso. Barcode: " + produtoTeste.getBarcode());

        // Passo 2: Simular Venda de 3 unidades
        // Como o sistema atual (UI) não chama o backend para processar a venda,
        // apenas simulamos que a UI fez o seu papel visual.
        // A falha esperada é que o Backend NÃO foi acionado para atualizar o saldo.
        System.out.println("- Simulando venda de 3 unidades na interface...");

        // Passo 3: Verificar Estoque Final no Banco
        Response resBusca = new Response();
        // Buscamos especificamente pelo nome
        ArrayList<ProductDTO> produtos = controller.searchProductsByName(produtoTeste.getProductName(), resBusca);
        
        // Filtra para garantir que pegamos o produto com O NOSSO código de barras (caso haja homônimos)
        ProductDTO produtoAtualizado = null;
        if (produtos != null) {
            for (ProductDTO p : produtos) {
                if (p.getBarcode().equals(produtoTeste.getBarcode())) {
                    produtoAtualizado = p;
                    break;
                }
            }
        }
        
        assertNotNull("O produto cadastrado deveria ser encontrado no banco", produtoAtualizado);
        System.out.println("- Estoque encontrado no banco: " + produtoAtualizado.getStockQuantity());

        // A VALIDAÇÃO CRÍTICA (Onde esperamos a FALHA)
        // Esperamos 7.0 (10 - 3), mas como a feature não existe, virá 10.0
        assertEquals("FALHA ESPERADA: O estoque deveria ter baixado para 7.0 após a venda, mas o sistema não atualizou.", 
                7.0, // Esperado
                produtoAtualizado.getStockQuantity(), // Atual
                0.01); // Delta
    }
}