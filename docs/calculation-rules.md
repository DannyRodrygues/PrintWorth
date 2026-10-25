
# PrintWorth — Regras de Cálculo

**Status:** implementação em desenvolvimento
**Última atualização:** 23/10/2026

## 1. Objetivo

O PrintWorth calcula o custo de produção de peças
impressas em 3D e sugere preços de venda.

O aplicativo disponibiliza dois métodos:

1. Lucro sobre o custo (COST_MARKUP).
2. Margem sobre a venda (SALE_MARGIN).

O método padrão é Lucro sobre o custo.

## 2. Custos considerados

O custo base é formado por:

- Filamento consumido.
- Energia elétrica.
- Desgaste e manutenção da impressora.
- Embalagem opcional.

Outras despesas não são adicionadas automaticamente.

## 3. Custo do filamento

custoFilamento =
(pesoEmGramas / 1000) * precoPorKg

Exemplo:

100 g a R$ 100,00/kg = R$ 10,00.

O peso deve considerar suportes, brim e outros
consumos atribuíveis à impressão.

## 4. Energia elétrica

tempoEmHoras = horas + minutos / 60

consumoKWh =
(potenciaMediaWatts / 1000) * tempoEmHoras

custoEnergia = consumoKWh * tarifaPorKWh

A potência média é informada pelo usuário.

## 5. Desgaste

custoDesgaste =
tempoEmHoras * desgastePorHora

A taxa de desgaste é definida pelo usuário.

## 6. Embalagem

custoEmbalagem = valor informado

Quando não houver embalagem, considerar zero.

## 7. Custo base

custoBase =
custoFilamento
+ custoEnergia
+ custoDesgaste
+ custoEmbalagem

Os componentes são arredondados para centavos
antes da soma.

## 8. Método 1 — Lucro sobre o custo

Identificador: COST_MARKUP

Este método acrescenta o percentual desejado
ao custo de produção.

Sem comissão:

precoVenda =
custoBase * (1 + percentualLucro / 100)

Exemplo:

- Custo base: R$ 20,00.
- Lucro desejado: 100%.
- Preço sugerido: R$ 40,00.
- Lucro estimado: R$ 20,00.

Esse método aceita percentuais de 100%, 200%
ou superiores, dentro dos limites numéricos
do aplicativo.

## 9. Método 2 — Margem sobre a venda

Identificador: SALE_MARGIN

Este método determina quanto do preço final
de venda deverá sobrar após os custos informados
e a comissão.

Sem comissão:

precoVenda =
custoBase / (1 - margem / 100)

Exemplo:

- Custo base: R$ 20,00.
- Margem desejada: 50%.
- Preço sugerido: R$ 40,00.
- Lucro estimado: R$ 20,00.

Uma margem de 100% não é possível quando
existe custo de produção positivo.

A margem desejada deve ser inferior a 100%.

## 10. Diferença entre os dois métodos

Considere:

- Custo base: R$ 20,00.
- Preço de venda: R$ 40,00.
- Lucro estimado: R$ 20,00.

Lucro sobre o custo:

20 / 20 * 100 = 100%

Margem sobre a venda:

20 / 40 * 100 = 50%

Os dois percentuais estão corretos.

A diferença é a base utilizada para
calcular o percentual.

## 11. Comissão do ponto de venda

A comissão é opcional e incide sobre
o preço final de venda.

Quando desativada, seu valor é zero.

A comissão deve ser maior ou igual a zero
e inferior a 100%.

### Comissão no método Sobre o custo

precoVenda =
[custoBase * (1 + lucro / 100)]
/ (1 - comissao / 100)

### Comissão no método Sobre a venda

precoVenda =
custoBase
/ (1 - margem / 100 - comissao / 100)

No método Sobre a venda:

margem + comissao < 100%

Se a soma atingir ou ultrapassar 100%,
o cálculo será rejeitado.

## 12. Lucro estimado

valorComissao =
precoVenda * comissao / 100

lucroEstimado =
precoVenda - custoBase - valorComissao

O lucro estimado considera somente
os custos informados e a comissão.

Não deve ser confundido automaticamente
com o lucro líquido da empresa.

## 13. Margem efetiva

Independentemente do método escolhido,
o resultado apresenta a margem efetiva
sobre o preço de venda.

margemEfetiva =
(lucroEstimado / precoVenda) * 100

Quando o preço de venda for zero,
a margem efetiva será considerada
não aplicável.

## 14. Precisão monetária

O aplicativo utiliza aritmética inteira.

Representações:

- Valores monetários finais em centavos.
- Preços unitários com quatro casas decimais.
- Percentuais em pontos-base.
- 100 pontos-base representam 1%.
- 10.000 pontos-base representam 100%.

Não utilizar Float ou Double para
representar valores monetários.

Política de arredondamento:

1. Custos individuais: HALF_UP.
2. Custo base: soma dos custos arredondados.
3. Receita líquida mínima no COST_MARKUP:
   arredondamento para cima.
4. Preço sugerido: arredondamento para cima.
5. Comissão: HALF_UP.
6. Lucro: diferença entre preço, custos e comissão.

A implementação deve manter a igualdade:

precoVenda =
custoBase + comissao + lucroEstimado

Valores que excedam os limites numéricos
suportados devem ser tratados como erro.

A ampliação dos testes de valores extremos
permanece pendente.

## 15. Exemplos de referência

Considere o custo base de R$ 17,75.

### Exemplo A — Sobre o custo

- Lucro desejado: 100%.
- Comissão: desativada.
- Preço sugerido: R$ 35,50.
- Lucro estimado: R$ 17,75.
- Margem efetiva: 50%.

### Exemplo B — Sobre o custo com comissão

- Lucro desejado: 100%.
- Comissão: 10%.
- Preço sugerido: R$ 39,45.
- Comissão calculada: R$ 3,95.
- Lucro estimado: R$ 17,75.

### Exemplo C — Sobre a venda

- Margem desejada: 20%.
- Comissão: 10%.
- Preço sugerido: R$ 25,36.
- Comissão calculada: R$ 2,54.
- Lucro estimado: R$ 5,07.
- Margem efetiva após arredondamento: 19,99%.

## 16. Validações principais

- Peso do filamento maior que zero.
- Tempo total maior que zero.
- Minutos entre 0 e 59.
- Potência média positiva.
- Custos não negativos.
- Percentual desejado não negativo.
- Comissão inferior a 100%.
- Margem + comissão inferior a 100%
  quando utilizado SALE_MARGIN.
- Entradas numéricas válidas.
- Proteção contra operações numéricas
  que excedam os limites suportados.

## 17. Implementação técnica

Modelos e componentes principais:

- PricingMethod.kt
- PrintCalculationInput.kt
- PrintCalculationResult.kt
- Money.kt
- MoneyRate.kt
- FixedPointMath.kt
- CalculatePrintCostUseCase.kt
- CalculatorFormValidator.kt
- CalculatorViewModel.kt

Testes automatizados:

- FixedPointMathTest.kt
- CalculatePrintCostUseCaseTest.kt
- DecimalInputParserTest.kt
- CalculatorFormValidatorTest.kt
- CalculatorViewModelTest.kt

Comando:

./gradlew :shared:testAndroidHostTest

## 18. Interface e orientação ao usuário

O formulário oferece a escolha:

- Sobre o custo.
- Sobre a venda.

Há uma ajuda contextual explicando
a diferença entre os dois métodos.

A seção "Sobre o PrintWorth" apresenta
explicações educativas, exemplos e
limitações dos cálculos.

## 19. Limitações atuais

O aplicativo ainda não inclui
automaticamente:

- Impostos.
- Mão de obra.
- Frete.
- Aluguel.
- Falhas e perdas de impressão.
- Outras despesas operacionais.

O resultado deve ser interpretado como
uma estimativa baseada nos dados fornecidos.


---

## 20. Cálculos com múltiplas moedas

**Status:** Implementado e validado no Android.

### 20.1 Moedas suportadas

O PrintWorth permite realizar cálculos financeiros
utilizando três moedas:

| Código | Moeda | Símbolo |
|---|---|---|
| BRL | Real brasileiro | R$ |
| USD | Dólar americano | US$ |
| EUR | Euro | € |

A moeda padrão é BRL.

### 20.2 Regra de funcionamento

O usuário informa todos os custos diretamente
na moeda selecionada.

O aplicativo não realiza conversão cambial.

Exemplo:

Se o usuário selecionar EUR e informar
10,00 como preço do filamento por quilograma,
o valor será interpretado como € 10,00/kg.

Todos os resultados serão apresentados em euros.

A mesma regra é aplicada ao Real e ao Dólar.

### 20.3 Fórmulas financeiras

As fórmulas do PrintWorth são independentes
da moeda selecionada.

Continuam sendo utilizadas as regras de:

- Custo do filamento.
- Custo de energia.
- Desgaste da impressora.
- Embalagem.
- Custo base.
- Lucro sobre o custo (COST_MARKUP).
- Margem sobre a venda (SALE_MARGIN).
- Comissão opcional.
- Lucro estimado.
- Margem efetiva.

A seleção da moeda não modifica os percentuais,
as fórmulas ou a política de arredondamento.

### 20.4 Precisão monetária

As três moedas suportadas possuem duas casas
decimais para valores monetários finais.

O motor financeiro utiliza:

- Valores monetários finais em centavos.
- Tarifas e preços unitários com quatro
  casas decimais.
- Percentuais representados em pontos-base.
- Aritmética inteira para preservar precisão.

Não são utilizados Float ou Double para
representar dinheiro.

Os valores em centavos representam a unidade
monetária mínima da moeda selecionada.

### 20.5 Exemplo de cálculo em euros

Considere os seguintes valores informados
diretamente em euros:

| Parâmetro | Valor |
|---|---:|
| Moeda | EUR |
| Filamento utilizado | 100 g |
| Preço do filamento | € 10,00/kg |
| Tempo de impressão | 5 horas |
| Potência média informada | 1.300 W |
| Tarifa de energia | € 1,00/kWh |
| Desgaste | € 1,00/h |
| Embalagem | € 2,00 |
| Método | Sobre o custo |
| Lucro desejado | 100% |
| Comissão | Desativada |

A potência de 1.300 W reproduz o valor
informado neste cenário de teste.
Não representa uma recomendação de
consumo médio para uma impressora específica.

#### Custo do filamento

100 / 1000 × 10,00 = € 1,00

#### Custo de energia

1.300 / 1000 × 5 = 6,5 kWh

6,5 × 1,00 = € 6,50

#### Custo de desgaste

5 × 1,00 = € 5,00

#### Custo base

€ 1,00 + € 6,50 + € 5,00 + € 2,00

Custo base = € 14,50

#### Preço sugerido

Método: COST_MARKUP

Lucro desejado: 100%

€ 14,50 × (1 + 100 / 100)

Preço sugerido = € 29,00

#### Lucro estimado

€ 29,00 - € 14,50 = € 14,50

#### Margem efetiva

14,50 / 29,00 × 100 = 50%

### 20.6 Resultado validado

| Descrição | Resultado |
|---|---:|
| Filamento | € 1,00 |
| Energia | € 6,50 |
| Desgaste | € 5,00 |
| Embalagem | € 2,00 |
| Custo base | € 14,50 |
| Preço sugerido | € 29,00 |
| Lucro estimado | € 14,50 |
| Margem efetiva | 50% |

O cenário acima foi conferido manualmente
durante os testes do aplicativo Android.

### 20.7 Alteração da moeda

Quando o usuário altera a moeda do cálculo,
os valores monetários anteriores são limpos
após confirmação.

Essa operação evita interpretar valores
informados em uma moeda como se
pertencessem a outra.

A alteração preserva os dados não monetários,
incluindo impressora, peso, tempo e percentuais.

Um resultado anterior é invalidado.

### 20.8 Formatação dos resultados

A apresentação monetária é realizada
pelo componente MoneyFormatter.

Exemplos:

| Moeda | Valor formatado |
|---|---|
| BRL | R$ 1.234,56 |
| USD | US$ 1,234.56 |
| EUR | € 1.234,56 |

A formatação altera apenas a representação
visual do valor.

Não existe conversão entre moedas.

### 20.9 Validação

Foram realizados testes manuais com:

- Real brasileiro (BRL).
- Dólar americano (USD).
- Euro (EUR).

Os testes confirmaram:

- [x] Seleção das três moedas.
- [x] Atualização das unidades monetárias.
- [x] Cálculos financeiros consistentes.
- [x] Resultados apresentados na moeda escolhida.
- [x] Comissão e lucro apresentados na mesma moeda.
- [x] Limpeza dos valores ao alterar a moeda.

O MoneyFormatter possui testes automatizados
para as três moedas.

A ampliação dos testes automatizados de
integração permanece como atividade futura.