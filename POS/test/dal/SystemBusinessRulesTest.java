package dal;

import model.POSController;
import model.dto.ProductDTO;
import model.dto.Response;
import model.dto.Message;
import model.dto.MessageType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Teste de Sistema - Gabriel Custodio
 * * Objetivo: Validar se as regras de negócio críticas (Business Rules) estão 
 * sendo aplicadas corretamente pelo sistema antes de persistir dados.
 * * @author Gabriel
 */
public class SystemBusinessRulesTest {

    private POSController controller;
    private Response response;

    @Before
    public void setUp() {
        controller = new POSController();
        response = new Response();
    }

    /**
     * Teste 1: Bloqueio de Preço Negativo
     * * Pré-condições:
     * - O sistema deve estar inicializado
     * - O validador (CommonValidator) deve conter a regra de validação de preço
     * * Passos:
     * 1. Criar um objeto Produto (ProductDTO) com dados válidos
     * 2. Definir propositalmente o preço como negativo (-10.0)
     * 3. Submeter o produto para validação
     * 4. Verificar se a operação falhou (não foi bem-sucedida)
     * 5. Verificar se a mensagem de erro correta foi gerada
     * * Resultado Esperado: 
     * - O sistema deve rejeitar o cadastro (isSuccessfull = false)
     * - Deve existir uma mensagem informando "cannot be negative"
     */
    @Test
    public void testCadastroProdutoPrecoNegativo() {
        // Arrange (Preparação)
        ProductDTO produtoInvalido = new ProductDTO();
        produtoInvalido.setProductName("Produto Teste Negativo");
        produtoInvalido.setBarcode("999999");
        produtoInvalido.setPrice(-10.0); // Cenário de teste: preço inválido
        produtoInvalido.setStockQuantity(10);
        produtoInvalido.setCategoryId(1);
        produtoInvalido.setSupplierId(1);
        produtoInvalido.setQuantityType("counted");

        // Act (Ação)
        Response responseValidacao = new Response();
        // Chamamos diretamente o validador para isolar o teste da regra
        model.validators.CommonValidator.validateObject(produtoInvalido, responseValidacao);

        // Assert (Verificação)
        assertFalse("O cadastro não deve ser bem-sucedido com preço negativo", responseValidacao.isSuccessfull());
        
        boolean encontrouMensagemErro = false;
        for (Message msg : responseValidacao.messagesList) {
            // Verifica se a mensagem esperada está presente na lista de erros
            if (msg.message.contains("cannot be negative")) {
                encontrouMensagemErro = true;
                break;
            }
        }
            
        assertTrue("Deve haver uma mensagem de erro sobre preço negativo", encontrouMensagemErro);
        
        System.out.println("✓ Teste 1 PASSOU: Sistema rejeitou corretamente o valor -10.0");
    }
    
    /**
     * Teste 2: Bloqueio de Venda com Estoque Zero
     * * Pré-condições:
     * - Produto existe no banco de dados
     * - Quantidade em estoque é 0.0
     * * Passos:
     * 1. Simular um cenário de venda
     * 2. Definir o estoque atual do produto como 0.0
     * 3. Tentar adicionar 1 item ao carrinho
     * 4. Validar a operação usando o CommonValidator
     * * Resultado Esperado:
     * - O sistema deve bloquear a operação (isSuccessfull = false)
     * - Deve retornar mensagem "Product out of stock"
     */
    @Test
    public void testVendaEstoqueZero() {
        // Arrange
        double estoqueAtual = 0.0; // Simula produto esgotado
        double quantidadeDesejada = 1.0;
        Response responseValidacao = new Response();

        // Act
        // Chamamos o método estático que criamos no CommonValidator
        model.validators.CommonValidator.validateStock(estoqueAtual, quantidadeDesejada, responseValidacao);

        // Assert
        assertFalse("Não deve permitir venda se estoque for 0", responseValidacao.isSuccessfull());
        
        boolean encontrouErroEstoque = false;
        for (Message msg : responseValidacao.messagesList) {
            // Verifica se a mensagem contém "out of stock"
            if (msg.message.contains("out of stock")) {
                encontrouErroEstoque = true;
                break;
            }
        }
        
        assertTrue("Deve retornar mensagem de erro de estoque zero", encontrouErroEstoque);
        System.out.println("✓ Teste 2 PASSOU: Sistema bloqueou venda de produto com estoque 0");
    }
}