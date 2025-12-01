/**
 * ================================================================================
 * TESTES DE CAIXA PRETA (Baseados em Cenários)
 * ================================================================================
 *
 * Objetivo: Testar o comportamento do sistema (entradas/saídas) sem conhecer
 * o código interno. Foco em cenários de uso do mundo real.
 *
 * COMO EXECUTAR:
 * 1. cd C:\Users\55549\Desktop\Github\projeto-teste-de-software\POS
 * 2. javac TesteCaixaPreta.java
 * 3. java TesteCaixaPreta
 *
 * ================================================================================
 */

import java.util.*;

public class TesteCaixaPreta {

    static int testesExecutados = 0;
    static int testesPassados = 0;

    public static void main(String[] args) {
        System.out.println("\n");
        System.out.println("=".repeat(80));
        System.out.println("              TESTES DE CAIXA PRETA - CENÁRIOS DE USO");
        System.out.println("=".repeat(80));
        System.out.println();
        pausar(500);

        System.out.println("┏" + "━".repeat(78) + "┓");
        System.out.println("┃" + centro("TESTE COMPORTAMENTAL", 78) + "┃");
        System.out.println("┃" + centro("Validando entradas e saídas esperadas", 78) + "┃");
        System.out.println("┗" + "━".repeat(78) + "┛");
        System.out.println();
        pausar(500);

        // Executar cenários
        cenarioAutenticacao();
        cenarioVendaInterrompida();
        cenarioEstoqueInsuficiente();
        cenarioProdutoInvalido();

        // Resumo
        resumoFinal();
    }

    /**
     * CENÁRIO 1: Autenticação
     */
    static void cenarioAutenticacao() {
        System.out.println("═".repeat(80));
        System.out.println("CENÁRIO 1: AUTENTICAÇÃO DE USUÁRIO");
        System.out.println("═".repeat(80));
        System.out.println();
        pausar(300);

        // Caso 1.1: Usuário correto, senha errada
        System.out.println("─".repeat(80));
        System.out.println("📝 Caso 1.1: Usuário correto, senha incorreta");
        System.out.println("─".repeat(80));
        String usuario1 = "admin";
        String senha1 = "senhaerrada";

        System.out.println("   Entrada:");
        System.out.println("      Usuário: " + usuario1);
        System.out.println("      Senha:   " + senha1);
        pausar(200);

        String resultado1 = fazerLogin(usuario1, senha1);
        String esperado1 = "Credenciais inválidas";

        System.out.println();
        System.out.println("   Saída:");
        System.out.println("      Retorno:  " + resultado1);
        System.out.println("      Esperado: " + esperado1);

        verificar(resultado1.equals(esperado1), "Login com senha errada");
        if (resultado1.equals(esperado1)) {
            System.out.println("      ✅ PASSOU!");
        }
        System.out.println();
        pausar(500);

        // Caso 1.2: Usuário inexistente
        System.out.println("─".repeat(80));
        System.out.println("📝 Caso 1.2: Usuário inexistente");
        System.out.println("─".repeat(80));
        String usuario2 = "usuarioqueNaoExiste";
        String senha2 = "qualquersenha";

        System.out.println("   Entrada:");
        System.out.println("      Usuário: " + usuario2);
        System.out.println("      Senha:   " + senha2);
        pausar(200);

        String resultado2 = fazerLogin(usuario2, senha2);
        String esperado2 = "Usuário não encontrado";

        System.out.println();
        System.out.println("   Saída:");
        System.out.println("      Retorno:  " + resultado2);
        System.out.println("      Esperado: " + esperado2);

        verificar(resultado2.equals(esperado2), "Login com usuário inexistente");
        if (resultado2.equals(esperado2)) {
            System.out.println("      ✅ PASSOU!");
        }
        System.out.println();
        pausar(500);

        // Caso 1.3: Campos vazios
        System.out.println("─".repeat(80));
        System.out.println("📝 Caso 1.3: Campos de login vazios");
        System.out.println("─".repeat(80));
        String usuario3 = "";
        String senha3 = "";

        System.out.println("   Entrada:");
        System.out.println("      Usuário: (vazio)");
        System.out.println("      Senha:   (vazio)");
        pausar(200);

        String resultado3 = fazerLogin(usuario3, senha3);
        String esperado3 = "Campos obrigatórios";

        System.out.println();
        System.out.println("   Saída:");
        System.out.println("      Retorno:  " + resultado3);
        System.out.println("      Esperado: " + esperado3);

        verificar(resultado3.equals(esperado3), "Login com campos vazios");
        if (resultado3.equals(esperado3)) {
            System.out.println("      ✅ PASSOU!");
        }
        System.out.println();
        pausar(500);

        // Caso 1.4: Login correto
        System.out.println("─".repeat(80));
        System.out.println("📝 Caso 1.4: Credenciais corretas");
        System.out.println("─".repeat(80));
        String usuario4 = "admin";
        String senha4 = "12345";

        System.out.println("   Entrada:");
        System.out.println("      Usuário: " + usuario4);
        System.out.println("      Senha:   " + senha4);
        pausar(200);

        String resultado4 = fazerLogin(usuario4, senha4);
        String esperado4 = "Login bem-sucedido";

        System.out.println();
        System.out.println("   Saída:");
        System.out.println("      Retorno:  " + resultado4);
        System.out.println("      Esperado: " + esperado4);

        verificar(resultado4.equals(esperado4), "Login com credenciais corretas");
        if (resultado4.equals(esperado4)) {
            System.out.println("      ✅ PASSOU!");
        }
        System.out.println();

        System.out.println("✅ CENÁRIO DE AUTENTICAÇÃO CONCLUÍDO!");
        System.out.println();
    }

    /**
     * CENÁRIO 2: Venda Interrompida
     */
    static void cenarioVendaInterrompida() {
        System.out.println("═".repeat(80));
        System.out.println("CENÁRIO 2: VENDA INTERROMPIDA (CANCELADA)");
        System.out.println("═".repeat(80));
        System.out.println();
        pausar(300);

        System.out.println("📝 Cenário: Iniciar venda, adicionar itens, cancelar");
        System.out.println();

        // Estoque inicial
        Map<String, Integer> estoqueInicial = new HashMap<>();
        estoqueInicial.put("Café", 50);
        estoqueInicial.put("Açúcar", 100);

        System.out.println("   Estoque Inicial:");
        System.out.println("      Café:   " + estoqueInicial.get("Café") + " unidades");
        System.out.println("      Açúcar: " + estoqueInicial.get("Açúcar") + " unidades");
        System.out.println();
        pausar(500);

        // Simular venda
        System.out.println("   Ação 1: Adicionar 2 Cafés ao carrinho");
        System.out.println("   Ação 2: Adicionar 5 Açúcares ao carrinho");
        System.out.println("   Ação 3: CANCELAR a venda");
        System.out.println();
        pausar(500);

        // Executar cancelamento
        Map<String, Integer> estoqueFinal = cancelarVenda(estoqueInicial);

        System.out.println("   Estoque Final (após cancelamento):");
        System.out.println("      Café:   " + estoqueFinal.get("Café") + " unidades");
        System.out.println("      Açúcar: " + estoqueFinal.get("Açúcar") + " unidades");
        System.out.println();

        // Verificar
        boolean estoqueIntacto = estoqueFinal.get("Café") == 50 &&
                                 estoqueFinal.get("Açúcar") == 100;

        verificar(estoqueIntacto, "Estoque não deve mudar em venda cancelada");
        if (estoqueIntacto) {
            System.out.println("   ✅ PASSOU! Estoque permaneceu intacto após cancelamento");
        }
        System.out.println();

        System.out.println("✅ CENÁRIO DE VENDA INTERROMPIDA CONCLUÍDO!");
        System.out.println();
    }

    /**
     * CENÁRIO 3: Estoque Insuficiente
     */
    static void cenarioEstoqueInsuficiente() {
        System.out.println("═".repeat(80));
        System.out.println("CENÁRIO 3: TENTATIVA DE VENDA COM ESTOQUE INSUFICIENTE");
        System.out.println("═".repeat(80));
        System.out.println();
        pausar(300);

        System.out.println("📝 Cenário: Tentar vender mais que o disponível em estoque");
        System.out.println();

        int estoqueDisponivel = 3;
        int qtdSolicitada = 5;

        System.out.println("   Produto: Café Premium");
        System.out.println("   Estoque disponível: " + estoqueDisponivel + " unidades");
        System.out.println("   Quantidade solicitada: " + qtdSolicitada + " unidades");
        System.out.println();
        pausar(500);

        String resultado = tentarVenda(estoqueDisponivel, qtdSolicitada);
        String esperado = "Estoque insuficiente";

        System.out.println("   Resultado:");
        System.out.println("      Retorno:  " + resultado);
        System.out.println("      Esperado: " + esperado);

        verificar(resultado.equals(esperado), "Sistema deve impedir venda sem estoque");
        if (resultado.equals(esperado)) {
            System.out.println("      ✅ PASSOU! Sistema bloqueou venda corretamente");
        }
        System.out.println();

        System.out.println("✅ CENÁRIO DE ESTOQUE INSUFICIENTE CONCLUÍDO!");
        System.out.println();
    }

    /**
     * CENÁRIO 4: Produto Inválido
     */
    static void cenarioProdutoInvalido() {
        System.out.println("═".repeat(80));
        System.out.println("CENÁRIO 4: CADASTRO DE PRODUTO INVÁLIDO");
        System.out.println("═".repeat(80));
        System.out.println();
        pausar(300);

        System.out.println("📝 Cenário: Tentar cadastrar produto com dados inválidos");
        System.out.println();

        // Produto com preço negativo
        System.out.println("   Produto: 'Teste'");
        System.out.println("   Preço: -10.00 (NEGATIVO)");
        System.out.println();
        pausar(500);

        String resultado = cadastrarProduto("Teste", -10.00);
        String esperado = "Preço inválido";

        System.out.println("   Resultado:");
        System.out.println("      Retorno:  " + resultado);
        System.out.println("      Esperado: " + esperado);

        verificar(resultado.equals(esperado), "Sistema deve rejeitar preço negativo");
        if (resultado.equals(esperado)) {
            System.out.println("      ✅ PASSOU! Sistema validou entrada corretamente");
        }
        System.out.println();

        System.out.println("✅ CENÁRIO DE PRODUTO INVÁLIDO CONCLUÍDO!");
        System.out.println();
    }

    /**
     * Resumo Final
     */
    static void resumoFinal() {
        System.out.println("=".repeat(80));
        System.out.println("                         RESUMO FINAL");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("   Testes Executados: " + testesExecutados);
        System.out.println("   Testes Passados:   " + testesPassados);
        System.out.println("   Testes Falhados:   " + (testesExecutados - testesPassados));
        System.out.println();

        if (testesExecutados == testesPassados) {
            System.out.println("   ✅ TODOS OS CENÁRIOS DE CAIXA PRETA PASSARAM!");
            System.out.println();
            System.out.println("   📋 BENEFÍCIOS DOS TESTES DE CAIXA PRETA:");
            System.out.println("      → Testam perspectiva do usuário final");
            System.out.println("      → Independentes da implementação interna");
            System.out.println("      → Validam requisitos funcionais");
            System.out.println("      → Detectam problemas de UX e validação");
        }

        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println();
    }

    // ============================================================================
    // MÉTODOS SIMULADOS (representam o sistema real)
    // ============================================================================

    static String fazerLogin(String usuario, String senha) {
        if (usuario == null || usuario.isEmpty() || senha == null || senha.isEmpty()) {
            return "Campos obrigatórios";
        }

        // Banco simulado
        if (usuario.equals("admin")) {
            if (senha.equals("12345")) {
                return "Login bem-sucedido";
            } else {
                return "Credenciais inválidas";
            }
        }

        return "Usuário não encontrado";
    }

    static Map<String, Integer> cancelarVenda(Map<String, Integer> estoque) {
        // Em uma venda cancelada, o estoque permanece o mesmo
        return new HashMap<>(estoque);
    }

    static String tentarVenda(int estoqueDisponivel, int qtdSolicitada) {
        if (qtdSolicitada > estoqueDisponivel) {
            return "Estoque insuficiente";
        }
        return "Venda autorizada";
    }

    static String cadastrarProduto(String nome, double preco) {
        if (preco <= 0) {
            return "Preço inválido";
        }
        if (nome == null || nome.isEmpty()) {
            return "Nome obrigatório";
        }
        return "Produto cadastrado";
    }

    static void verificar(boolean condicao, String mensagem) {
        testesExecutados++;
        if (condicao) {
            testesPassados++;
        }
    }

    static String centro(String texto, int largura) {
        int espacos = (largura - texto.length()) / 2;
        return " ".repeat(Math.max(0, espacos)) + texto +
               " ".repeat(Math.max(0, largura - texto.length() - espacos));
    }

    static void pausar(int ms) {
        try { Thread.sleep(ms); } catch (Exception e) {}
    }
}
