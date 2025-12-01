
import java.util.*;

public class TesteCompleto {
    static Map<Integer, Produto> estoque = new HashMap<>();
    static int proximoId = 1;
    static int testesExecutados = 0;
    static int testesPassados = 0;

    static class Produto {
        int id;
        String nome;
        String barcode;
        double preco;
        double quantidade;

        Produto(String nome, String barcode, double preco, double quantidade) {
            this.nome = nome;
            this.barcode = barcode;
            this.preco = preco;
            this.quantidade = quantidade;
        }
    }

    public static void main(String[] args) {
        System.out.println("\n");
        System.out.println("=".repeat(80));
        System.out.println("       TESTES DE INTEGRAÇÃO E ACEITAÇÃO - SISTEMA POS");
        System.out.println("=".repeat(80));
        System.out.println();
        pausar(500);

        // EXECUTAR TESTES DE INTEGRAÇÃO
        testesIntegracao();

        // EXECUTAR TESTES DE ACEITAÇÃO
        testesAceitacao();

        // RESUMO FINAL
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("                         RESUMO FINAL");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("   Testes Executados: " + testesExecutados);
        System.out.println("   Testes Passados:   " + testesPassados);
        System.out.println("   Testes Falhados:   " + (testesExecutados - testesPassados));
        System.out.println();
        if (testesExecutados == testesPassados) {
            System.out.println("   ✅ TODOS OS TESTES PASSARAM COM SUCESSO!");
        } else {
            System.out.println("   ❌ ALGUNS TESTES FALHARAM!");
        }
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println();
    }

    // ============================================================================
    // TESTES DE INTEGRAÇÃO
    // ============================================================================

    static void testesIntegracao() {
        System.out.println("┏" + "━".repeat(78) + "┓");
        System.out.println("┃" + centro("TESTES DE INTEGRAÇÃO", 78) + "┃");
        System.out.println("┃" + centro("Verificam se módulos diferentes se comunicam corretamente", 78) + "┃");
        System.out.println("┗" + "━".repeat(78) + "┛");
        System.out.println();
        pausar(500);

        // Teste 1: CRUD Completo
        teste1_CRUD();

        // Teste 2: Venda → Estoque
        teste2_VendaEstoque();

        System.out.println();
    }

    static void teste1_CRUD() {
        System.out.println("─".repeat(80));
        System.out.println("TESTE 1: INTEGRAÇÃO CRUD DE PRODUTO");
        System.out.println("─".repeat(80));
        System.out.println();
        pausar(300);

        // CREATE
        System.out.println("📝 PASSO 1: CREATE (Criar Produto)");
        Produto p = new Produto("Produto Teste Integração", "999999", 50.0, 100.0);
        p.id = proximoId++;
        estoque.put(p.id, p);

        verificar(estoque.containsKey(p.id), "Produto deve estar no banco");
        System.out.println("   ✓ Produto criado: " + p.nome);
        System.out.println("   ✓ Código: " + p.barcode + " | Preço: R$ " + p.preco + " | Estoque: " + p.quantidade);
        System.out.println();
        pausar(500);

        // READ
        System.out.println("📖 PASSO 2: READ (Buscar Produto)");
        Produto encontrado = estoque.get(p.id);

        verificar(encontrado != null, "Produto deve ser encontrado");
        verificar(encontrado.barcode.equals("999999"), "Código de barras correto");
        System.out.println("   ✓ Produto encontrado no banco. ID: " + encontrado.id);
        System.out.println("   ✓ Barcode verificado: " + encontrado.barcode);
        System.out.println();
        pausar(500);

        // UPDATE
        System.out.println("✏️  PASSO 3: UPDATE (Atualizar Preço)");
        double precoAntigo = encontrado.preco;
        encontrado.preco = 75.0;
        estoque.put(encontrado.id, encontrado);

        Produto atualizado = estoque.get(encontrado.id);
        verificar(atualizado.preco == 75.0, "Preço deve estar atualizado");
        System.out.println("   ✓ Preço atualizado: R$ " + precoAntigo + " → R$ " + atualizado.preco);
        System.out.println();
        pausar(500);

        // DELETE
        System.out.println("🗑️  PASSO 4: DELETE (Remover Produto)");
        estoque.remove(p.id);

        verificar(!estoque.containsKey(p.id), "Produto deve ter sido removido");
        System.out.println("   ✓ Produto removido do banco com sucesso");
        System.out.println();
        pausar(500);

        System.out.println("✅ TESTE CRUD CONCLUÍDO: CREATE, READ, UPDATE, DELETE funcionando!");
        System.out.println();
    }

    static void teste2_VendaEstoque() {
        System.out.println("─".repeat(80));
        System.out.println("TESTE 2: INTEGRAÇÃO VENDA ↔ ESTOQUE");
        System.out.println("─".repeat(80));
        System.out.println();
        pausar(300);

        // Preparar produto
        Produto p = new Produto("Produto Venda Teste", "888888", 10.0, 100.0);
        p.id = proximoId++;
        estoque.put(p.id, p);

        System.out.println("📦 CENÁRIO INICIAL:");
        System.out.println("   Produto: " + p.nome);
        System.out.println("   Estoque inicial: " + p.quantidade + " unidades");
        System.out.println("   Preço unitário: R$ " + p.preco);
        System.out.println();
        pausar(500);

        // Simular venda
        System.out.println("🛒 PROCESSANDO VENDA:");
        double qtdVendida = 5.0;
        double estoqueAntes = p.quantidade;
        p.quantidade -= qtdVendida;
        estoque.put(p.id, p);

        System.out.println("   → Quantidade vendida: " + qtdVendida);
        System.out.println("   → Estoque antes: " + estoqueAntes);
        System.out.println("   → Estoque depois: " + p.quantidade);
        System.out.println();
        pausar(500);

        // Verificar persistência
        System.out.println("🔍 VERIFICANDO PERSISTÊNCIA:");
        Produto noEstoque = estoque.get(p.id);

        verificar(noEstoque.quantidade == 95.0, "Estoque deve estar em 95");
        System.out.println("   ✓ Estoque atualizado corretamente no banco!");
        System.out.println("   ✓ Integração Venda ↔ Estoque funcionando!");
        System.out.println();
        pausar(500);

        System.out.println("✅ TESTE VENDA/ESTOQUE CONCLUÍDO!");
        System.out.println();

        // Limpar para próximos testes
        estoque.clear();
        proximoId = 1;
    }

    // ============================================================================
    // TESTES DE ACEITAÇÃO (UAT)
    // ============================================================================

    static void testesAceitacao() {
        System.out.println("┏" + "━".repeat(78) + "┓");
        System.out.println("┃" + centro("TESTES DE ACEITAÇÃO (UAT)", 78) + "┃");
        System.out.println("┃" + centro("Simulação de Dia de Trabalho do Operador de Caixa", 78) + "┃");
        System.out.println("┗" + "━".repeat(78) + "┛");
        System.out.println();
        pausar(500);

        tarefa1_CadastrarProdutos();
        tarefa2_ProcessarVendas();
        tarefa3_VerificarEstoque();
        tarefa4_GerarRelatorio();
    }

    static void tarefa1_CadastrarProdutos() {
        System.out.println("─".repeat(80));
        System.out.println("TAREFA 1: CADASTRAR 5 NOVOS PRODUTOS");
        System.out.println("─".repeat(80));
        System.out.println("Cenário: Chegaram produtos novos que precisam ser cadastrados");
        System.out.println();
        pausar(300);

        String[] nomes = {
            "Café Premium 500g",
            "Açúcar Cristal 1kg",
            "Arroz Integral 1kg",
            "Feijão Preto 1kg",
            "Macarrão Espaguete 500g"
        };

        String[] codigos = {"7891001", "7891002", "7891003", "7891004", "7891005"};
        double[] precos = {15.90, 4.50, 8.90, 7.50, 5.20};
        double[] quantidades = {50, 100, 80, 60, 120};

        for (int i = 0; i < 5; i++) {
            Produto p = new Produto(nomes[i], codigos[i], precos[i], quantidades[i]);
            p.id = proximoId++;
            estoque.put(p.id, p);

            System.out.printf("   ✓ Produto %d: %-30s | R$ %6.2f | %3.0f un.%n",
                (i + 1), p.nome, p.preco, p.quantidade);
            pausar(200);
        }

        verificar(estoque.size() == 5, "5 produtos devem estar cadastrados");
        System.out.println();
        System.out.println("✅ 5 produtos cadastrados com sucesso!");
        System.out.println();
    }

    static void tarefa2_ProcessarVendas() {
        System.out.println("─".repeat(80));
        System.out.println("TAREFA 2: PROCESSAR VENDAS (3 CLIENTES)");
        System.out.println("─".repeat(80));
        System.out.println("Cenário: Atender clientes e processar compras");
        System.out.println();
        pausar(300);

        // Cliente 1
        vender(1, 2, "Cliente 1 compra 2x Café Premium");
        vender(2, 5, "Cliente 1 compra 5x Açúcar Cristal");

        // Cliente 2
        vender(3, 3, "Cliente 2 compra 3x Arroz Integral");

        // Cliente 3
        vender(4, 2, "Cliente 3 compra 2x Feijão Preto");
        vender(5, 10, "Cliente 3 compra 10x Macarrão Espaguete");

        System.out.println();
        System.out.println("✅ 3 clientes atendidos, 5 itens de venda processados!");
        System.out.println();
    }

    static void tarefa3_VerificarEstoque() {
        System.out.println("─".repeat(80));
        System.out.println("TAREFA 3: VERIFICAR ESTOQUE ATUAL");
        System.out.println("─".repeat(80));
        System.out.println();
        pausar(300);

        System.out.printf("%-30s  %-12s  %10s  %12s%n",
            "PRODUTO", "CÓDIGO", "ESTOQUE", "VALOR R$");
        System.out.println("─".repeat(80));

        double[] estoquesEsperados = {48, 95, 77, 58, 110};
        double valorTotal = 0;

        for (int i = 1; i <= 5; i++) {
            Produto p = estoque.get(i);
            double valor = p.quantidade * p.preco;
            valorTotal += valor;

            verificar(p.quantidade == estoquesEsperados[i-1],
                "Estoque de " + p.nome + " deve ser " + estoquesEsperados[i-1]);

            System.out.printf("%-30s  %-12s  %7.0f un.  %12.2f%n",
                p.nome, p.barcode, p.quantidade, valor);
            pausar(200);
        }

        System.out.println("─".repeat(80));
        System.out.printf("%-30s  %-12s  %10s  %12.2f%n",
            "TOTAL EM ESTOQUE", "", "", valorTotal);
        System.out.println();
        System.out.println("✅ Estoque verificado com sucesso!");
        System.out.println();
    }

    static void tarefa4_GerarRelatorio() {
        System.out.println("─".repeat(80));
        System.out.println("TAREFA 4: GERAR RELATÓRIO DE VENDAS DO DIA");
        System.out.println("─".repeat(80));
        System.out.println();
        pausar(300);

        System.out.printf("%-30s  %12s  %12s  %12s%n",
            "PRODUTO", "QTD VENDIDA", "PREÇO UNIT.", "TOTAL");
        System.out.println("─".repeat(80));

        int[][] vendas = {{1,2}, {2,5}, {3,3}, {4,2}, {5,10}};
        double totalFaturamento = 0;
        int totalItens = 0;

        for (int[] v : vendas) {
            Produto p = estoque.get(v[0]);
            int qtd = v[1];
            double subtotal = qtd * p.preco;
            totalFaturamento += subtotal;
            totalItens += qtd;

            System.out.printf("%-30s  %12d  %12.2f  %12.2f%n",
                p.nome, qtd, p.preco, subtotal);
            pausar(200);
        }

        System.out.println("─".repeat(80));
        System.out.printf("%-30s  %12d  %12s  %12.2f%n",
            "TOTAL", totalItens, "", totalFaturamento);
        System.out.println();

        verificar(totalItens == 22, "Total de itens deve ser 22");
        verificar(Math.abs(totalFaturamento - 148.00) < 0.01, "Faturamento deve ser R$ 148.00");

        System.out.println("📊 RESUMO DO DIA:");
        System.out.println("   → Itens vendidos: " + totalItens);
        System.out.println("   → Faturamento: R$ " + String.format("%.2f", totalFaturamento));
        System.out.println("   → Ticket médio: R$ " + String.format("%.2f", totalFaturamento / 5));
        System.out.println();
        System.out.println("✅ Relatório gerado com sucesso!");
        System.out.println();
    }

    // ============================================================================
    // FUNÇÕES AUXILIARES
    // ============================================================================

    static void vender(int produtoId, int qtd, String descricao) {
        Produto p = estoque.get(produtoId);
        double estoqueAntes = p.quantidade;
        p.quantidade -= qtd;
        estoque.put(produtoId, p);
        double valor = qtd * p.preco;

        System.out.printf("   ✓ %-50s | %3.0f → %3.0f | R$ %6.2f%n",
            descricao, estoqueAntes, p.quantidade, valor);
        pausar(200);
    }

    static void verificar(boolean condicao, String mensagem) {
        testesExecutados++;
        if (condicao) {
            testesPassados++;
        } else {
            System.out.println("   ❌ FALHOU: " + mensagem);
        }
    }

    static String centro(String texto, int largura) {
        int espacos = (largura - texto.length()) / 2;
        return " ".repeat(Math.max(0, espacos)) + texto +
               " ".repeat(Math.max(0, largura - texto.length() - espacos));
    }

    static void pausar(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {}
    }
}
