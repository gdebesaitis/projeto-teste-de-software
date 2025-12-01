package dal.db;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de Conexão - 	
 */
public class MySQLConnectionTest {
    
    private MySQLConnection mySQLConnection;
    
    /**
     * Teste: Conexão Bem-Sucedida
     * Requisito: Iniciar com serviço ativo e verificar conexão.
     */
    @Test
    @DisplayName("Conexão Bem-Sucedida")
    public void testSuccessfulConnection() {
        // Arrange: Credenciais corretas (ajuste a senha se necessário)
        mySQLConnection = new MySQLConnection("pos", "root", "12345");
        
        // Act
        Connection connection = mySQLConnection.getConnection();
        
        // Assert: A conexão deve existir (não ser nula)
        assertNotNull(connection, "A conexão deveria ter sido realizada com sucesso.");
    }
    
    /**
     * Teste: Falha de Conexão (Simulação de Serviço Offline)
     * Requisito: Tentar iniciar com serviço desligado (simulado por porta/banco errado).
     */
    @Test
    @DisplayName("Falha de Conexão (Serviço Offline)")
    public void testConnectionFailure() {
        // Arrange: Configuração errada para simular banco offline/inacessível
        mySQLConnection = new MySQLConnection("banco_inexistente", "root", "12345");
        
        // Act
        Connection connection = mySQLConnection.getConnection();
        
        // Assert: O sistema não deve travar, apenas retornar null ou lidar com erro
        assertNull(connection, "A conexão não deveria ocorrer com serviço 'offline'.");
    }
}