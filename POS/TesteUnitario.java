/**
 * ================================================================================
 * TESTES UNITÁRIOS
 * ================================================================================
 *
 * Objetivo: Testar as menores unidades lógicas de código (métodos isolados).
 * Valida cálculos e regras de negócio específicas.
 *
 * COMO EXECUTAR:
 * 1. cd C:\Users\55549\Desktop\Github\projeto-teste-de-software\POS
 * 2. javac TesteUnitario.java
 * 3. java TesteUnitario
 *
 * ================================================================================
 */

import java.util.*;

public class TesteUnitario {

    static int testesExecutados = 0;
    static int testesPassados = 0;

    public static void main(String[] args) {
        System.out.println("\n");
        System.out.println("=".repeat(80));
        System.out.println("                    TESTES UNITÁRIOS");
        System.out.println("=".repeat(80));
        System.out.println();
        pausar(500);

        System.out.println("┏" + "━".repeat(78) + "┓");
        System.out.println("┃" + centro("TESTE DE UNIDADES ISOLADAS", 78) + "┃");
        System.out.println("┃" + centro("Validando métodos e cálculos individuais", 78) + "┃");
        System.out.println("┗" + "━".repeat(78) + "┛");
        System.out.println();
        pausar(500);

        // Executar testes
        testeCalculoVenda();
        testeValidacaoProduto();
        testeCalculoDesconto();
        testeCalculoTroco();

        // Resumo
        resumoFinal();
    }

    /**
     * TESTE 1: Cálculo de Total de Venda
     */
    static void testeCalculoVenda() {
        System.out.println("─".repeat(80));
        System.out.println("TESTE 1: CÁLCULO DE TOTAL DE VENDA");
        System.out.println("─".repeat(80));
        System.out.println("Testando método calcularTotal(itens, desconto)");
        System.out.println();
        pausar(300);

        // Caso 1: Venda simples sem desconto
        System.out.println("📝 Caso 1: Venda simples (3 itens, sem desconto)");
        List<ItemVenda> itens1 = new ArrayList<>();
        itens1.add(new ItemVenda("Café", 15.90, 2));
        itens1.add(new ItemVenda("Açúcar", 4.50, 5));
        itens1.add(new ItemVenda("Arroz", 8.90, 1));

        double total1 = calcularTotal(itens1, 0);
        double esperado1 = (15.90 * 2) + (4.50 * 5) + (8.90 * 1); // 31.80 + 22.50 + 8.90 = 63.20

        System.out.println("   → Café:   2 × R$ 15,90 = R$ " + (15.90 * 2));
        System.out.println("   → Açúcar: 5 × R$  4,50 = R$ " + (4.50 * 5));
        System.out.println("   → Arroz:  1 × R$  8,90 = R$ " + (8.90 * 1));
        System.out.println("   ─────────────────────────────");
        System.out.println("   Total calculado: R$ " + String.format("%.2f", total1));
        System.out.println("   Total esperado:  R$ " + String.format("%.2f", esperado1));

        verificar(Math.abs(total1 - esperado1) < 0.01, "Cálculo de venda sem desconto");
        if (Math.abs(total1 - esperado1) < 0.01) {
            System.out.println("   ✅ PASSOU!");
        }
        System.out.println();
        pausar(500);

        // Caso 2: Venda com desconto
        System.out.println("📝 Caso 2: Venda com desconto de 10%");
        double desconto = 10.0; // 10%
        double total2 = calcularTotal(itens1, desconto);
        double esperado2 = esperado1 * (1 - desconto / 100); // 63.20 * 0.9 = 56.88

        System.out.println("   Subtotal:  R$ " + String.format("%.2f", esperado1));
        System.out.println("   Desconto:  " + desconto + "%");
        System.out.println("   Total:     R$ " + String.format("%.2f", total2));
        System.out.println("   Esperado:  R$ " + String.format("%.2f", esperado2));

        verificar(Math.abs(total2 - esperado2) < 0.01, "Cálculo de venda com desconto");
        if (Math.abs(total2 - esperado2) < 0.01) {
            System.out.println("   ✅ PASSOU!");
        }
        System.out.println();
        pausar(500);

        // Caso 3: Venda com valores decimais
        System.out.println("📝 Caso 3: Venda com quantidades decimais");
        List<ItemVenda> itens3 = new ArrayList<>();
        itens3.add(new ItemVenda("Queijo (kg)", 45.90, 0.5)); // meio quilo

        double total3 = calcularTotal(itens3, 0);
        double esperado3 = 45.90 * 0.5; // 22.95

        System.out.println("   → Queijo: 0,5 kg × R$ 45,90 = R$ " + String.format("%.2f", esperado3));
        System.out.println("   Total: R$ " + String.format("%.2f", total3));

        verificar(Math.abs(total3 - esperado3) < 0.01, "Cálculo com decimais");
        if (Math.abs(total3 - esperado3) < 0.01) {
            System.out.println("   ✅ PASSOU!");
        }
        System.out.println();

        System.out.println("✅ TESTE DE CÁLCULO DE VENDA CONCLUÍDO!");
        System.out.println();
    }

    /**
     * TESTE 2: Validação de Produto
     */
    static void testeValidacaoProduto() {
        System.out.println("─".repeat(80));
        System.out.println("TESTE 2: VALIDAÇÃO DE PRODUTO");
        System.out.println("─".repeat(80));
        System.out.println("Testando método produto.isValid()");
        System.out.println();
        pausar(300);

        // Produto válido
        System.out.println("📝 Caso 1: Produto válido");
        Produto p1 = new Produto("Café", "123456", 15.90, 10);
        boolean valido1 = p1.isValid();
        System.out.println("   Nome:   " + p1.nome);
        System.out.println("   Código: " + p1.codigo);
        System.out.println("   Preço:  R$ " + p1.preco);
        System.out.println("   Estoque:" + p1.estoque);
        System.out.println("   Válido: " + (valido1 ? "SIM ✓" : "NÃO ✗"));
        verificar(valido1, "Produto válido deve passar");
        System.out.println();
        pausar(500);

        // Produto inválido - preço negativo
        System.out.println("📝 Caso 2: Produto com preço negativo (inválido)");
        Produto p2 = new Produto("Produto X", "999", -10.0, 5);
        boolean valido2 = p2.isValid();
        System.out.println("   Preço: R$ " + p2.preco);
        System.out.println("   Válido: " + (valido2 ? "SIM ✓" : "NÃO ✗"));
        verificar(!valido2, "Produto com preço negativo deve falhar");
        if (!valido2) {
            System.out.println("   ✅ PASSOU! (corretamente rejeitado)");
        }
        System.out.println();
        pausar(500);

        // Produto inválido - estoque negativo
        System.out.println("📝 Caso 3: Produto com estoque negativo (inválido)");
        Produto p3 = new Produto("Produto Y", "888", 20.0, -5);
        boolean valido3 = p3.isValid();
        System.out.println("   Estoque: " + p3.estoque);
        System.out.println("   Válido: " + (valido3 ? "SIM ✓" : "NÃO ✗"));
        verificar(!valido3, "Produto com estoque negativo deve falhar");
        if (!valido3) {
            System.out.println("   ✅ PASSOU! (corretamente rejeitado)");
        }
        System.out.println();

        System.out.println("✅ TESTE DE VALIDAÇÃO CONCLUÍDO!");
        System.out.println();
    }

    /**
     * TESTE 3: Cálculo de Desconto
     */
    static void testeCalculoDesconto() {
        System.out.println("─".repeat(80));
        System.out.println("TESTE 3: CÁLCULO DE DESCONTO");
        System.out.println("─".repeat(80));
        System.out.println("Testando método calcularDesconto(valor, percentual)");
        System.out.println();
        pausar(300);

        // Caso 1: Desconto de 10%
        double valor1 = 100.0;
        double desc1 = 10.0;
        double resultado1 = calcularDesconto(valor1, desc1);
        double esperado1 = 10.0;

        System.out.println("📝 Caso 1: 10% de desconto em R$ 100,00");
        System.out.println("   Desconto: R$ " + resultado1);
        System.out.println("   Esperado: R$ " + esperado1);
        verificar(Math.abs(resultado1 - esperado1) < 0.01, "Desconto de 10%");
        if (Math.abs(resultado1 - esperado1) < 0.01) {
            System.out.println("   ✅ PASSOU!");
        }
        System.out.println();
        pausar(500);

        // Caso 2: Desconto de 0%
        double resultado2 = calcularDesconto(100.0, 0);
        System.out.println("📝 Caso 2: 0% de desconto em R$ 100,00");
        System.out.println("   Desconto: R$ " + resultado2);
        verificar(resultado2 == 0, "Desconto zero");
        if (resultado2 == 0) {
            System.out.println("   ✅ PASSOU!");
        }
        System.out.println();

        System.out.println("✅ TESTE DE DESCONTO CONCLUÍDO!");
        System.out.println();
    }

    /**
     * TESTE 4: Cálculo de Troco
     */
    static void testeCalculoTroco() {
        System.out.println("─".repeat(80));
        System.out.println("TESTE 4: CÁLCULO DE TROCO");
        System.out.println("─".repeat(80));
        System.out.println("Testando método calcularTroco(total, valorPago)");
        System.out.println();
        pausar(300);

        double total = 47.80;
        double pago = 50.00;
        double troco = calcularTroco(total, pago);
        double esperado = 2.20;

        System.out.println("📝 Caso: Cliente paga R$ 50,00 em compra de R$ 47,80");
        System.out.println("   Total:      R$ " + String.format("%.2f", total));
        System.out.println("   Pago:       R$ " + String.format("%.2f", pago));
        System.out.println("   Troco:      R$ " + String.format("%.2f", troco));
        System.out.println("   Esperado:   R$ " + String.format("%.2f", esperado));

        verificar(Math.abs(troco - esperado) < 0.01, "Cálculo de troco");
        if (Math.abs(troco - esperado) < 0.01) {
            System.out.println("   ✅ PASSOU!");
        }
        System.out.println();

        System.out.println("✅ TESTE DE TROCO CONCLUÍDO!");
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
            System.out.println("   ✅ TODOS OS TESTES UNITÁRIOS PASSARAM!");
            System.out.println();
            System.out.println("   📋 BENEFÍCIOS DOS TESTES UNITÁRIOS:");
            System.out.println("      → Detectam bugs em unidades isoladas");
            System.out.println("      → Validam cálculos e regras de negócio");
            System.out.println("      → Executam rapidamente (milissegundos)");
            System.out.println("      → Facilitam refatoração com segurança");
        }

        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println();
    }

    // ============================================================================
    // CLASSES E MÉTODOS AUXILIARES (simulam o sistema real)
    // ============================================================================

    static class ItemVenda {
        String nome;
        double preco;
        double quantidade;

        ItemVenda(String nome, double preco, double quantidade) {
            this.nome = nome;
            this.preco = preco;
            this.quantidade = quantidade;
        }
    }

    static class Produto {
        String nome, codigo;
        double preco, estoque;

        Produto(String nome, String codigo, double preco, double estoque) {
            this.nome = nome;
            this.codigo = codigo;
            this.preco = preco;
            this.estoque = estoque;
        }

        boolean isValid() {
            return preco > 0 && estoque >= 0 && nome != null && !nome.isEmpty();
        }
    }

    static double calcularTotal(List<ItemVenda> itens, double descontoPercent) {
        double subtotal = 0;
        for (ItemVenda item : itens) {
            subtotal += item.preco * item.quantidade;
        }
        double desconto = subtotal * (descontoPercent / 100);
        return subtotal - desconto;
    }

    static double calcularDesconto(double valor, double percentual) {
        return valor * (percentual / 100);
    }

    static double calcularTroco(double total, double valorPago) {
        return valorPago - total;
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
