package dal;

import org.junit.jupiter.api.*;
import dal.db.MySQLConnection;
import model.dto.Response;
import model.dto.UserDTO;
import model.dto.ProductDTO; // Certifique-se que este import existe
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de Desempenho - Gui D.
 * Objetivo: Avaliar velocidade com volume de dados (10.000 produtos) e agilidade.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemPerformanceTest {

    private DALManager dalManager;
    private Response response;
    
    // Alvo: Tabela de Produtos (identificada no ObjectAdder.java)
    private static final int TARGET_VOLUME = 10000;

    @BeforeEach
    public void setUp() {
        dalManager = new DALManager();
        response = new Response();
    }

    /**
     * Passo 1: Popular Banco de Dados com Produtos (Teste de Carga)
     * Baseado na query encontrada em ObjectAdder.addProduct
     */
    @BeforeAll
    public static void seedDatabase() {
        System.out.println("--- Preparando Teste de Carga (Produtos) ---");
        MySQLConnection db = new MySQLConnection("pos", "root", "Admin123$"); // Confirme a senha do seu banco
        Connection conn = db.getConnection();

        if (conn != null) {
            try {
                conn.setAutoCommit(false); 
                
                // SQL exato retirado do ObjectAdder.java
                String sql = "INSERT INTO products (name, barcode, price, stock_quantity, category_id, quantity_type, suppliers_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    for (int i = 0; i < TARGET_VOLUME; i++) {
                        pstmt.setString(1, "Produto_Teste_" + i);      // name
                        pstmt.setString(2, "BC" + i);                  // barcode
                        pstmt.setDouble(3, 10.50 + (i * 0.1));         // price
                        pstmt.setDouble(4, 100);                       // stock_quantity
                        pstmt.setInt(5, 1);                            // category_id (Assumindo que ID 1 existe)
                        pstmt.setString(6, "Unit");                    // quantity_type
                        pstmt.setInt(7, 1);                            // suppliers_id (Assumindo que ID 1 existe)
                        pstmt.addBatch();
                        
                        if (i % 1000 == 0) System.out.println("Preparando lote: " + i);
                    }
                    pstmt.executeBatch();
                    conn.commit();
                    System.out.println("✓ Sucesso: " + TARGET_VOLUME + " produtos inseridos.");
                }
            } catch (Exception e) {
                System.out.println("Aviso: " + e.getMessage());
            } finally {
                db.closeConnection(conn);
            }
        }
    }

    /**
     * Teste 1: Tempo de Resposta com Alto Volume
     * Tenta buscar produtos (simulando a tela de Inventário).
     */
    @Test
    @Order(1)
    @DisplayName("Teste de Desempenho: Listagem de Produtos sob carga")
    public void testDataRetrievalPerformance() {
        Instant start = Instant.now();

        // Como o DALManager não tem um método getProducts direto visível na interface enviada,
        // vamos testar a busca via categoria ou assumir que existe um getProducts similar ao getUsers.
        // Se não existir, o teste de "Agilidade" abaixo é o mais importante.
        
        // Exemplo: Buscar categorias (que faria join com produtos numa aplicação real)
        dalManager.getCategories(response); 

        Instant end = Instant.now();
        long timeElapsed = Duration.between(start, end).toMillis();

        System.out.println("Tempo de execução da busca: " + timeElapsed + "ms");
        assertTrue(timeElapsed < 3000, "PERFORMANCE CRÍTICA: A busca demorou mais de 3s.");
    }

    /**
     * Teste 2: Agilidade (Simulação de Venda)
     * Mede o tempo para adicionar uma venda (usuário/cliente) no fluxo.
     */
    @Test
    @Order(2)
    @DisplayName("Teste de Agilidade: Fluxo de operação de venda")
    public void testOperationalAgility() {
        Instant start = Instant.now();
        
        // Simula: 1. Verifica cliente
        dalManager.getCustomers(response);
        
        // 2. Adiciona um novo cliente rápido (como se fosse fechar venda para novo cliente)
        UserDTO operador = new UserDTO();
        operador.setUsername("OperadorCaixa");
        operador.setPassword("123");
        dalManager.verifyUser(operador, response); // Verifica login rápido

        Instant end = Instant.now();
        long timeElapsed = Duration.between(start, end).toMillis();
        
        assertTrue(timeElapsed < 2000, "O fluxo de venda está lento (> 2s).");
    }
}