# PrintWorth — Regras de cálculo

Status: especificação inicial

## 1. Convenções

Moeda inicial: real brasileiro (BRL).

Unidades:
- Peso em gramas.
- Preço do filamento em reais por quilograma.
- Duração informada em horas e minutos.
- Potência média em watts.
- Tarifa de energia em reais por quilowatt-hora.
- Desgaste em reais por hora.
- Margem e comissão informadas em percentual.

Para calcular, dividir os percentuais por 100.
Exemplo: 20% corresponde a 0,20.

## 2. Duração

tempoEmHoras = horas + minutos / 60

Exemplo:
2 horas e 30 minutos = 2,5 horas.

A duração deve ser maior que zero.

## 3. Filamento

custoFilamento = (pesoEmGramas / 1000) × precoPorKg

Exemplo:
100 g de filamento a R$ 100,00/kg custam R$ 10,00.

## 4. Energia

consumoEmKWh = (potenciaMediaEmWatts / 1000) × tempoEmHoras

custoEnergia = consumoEmKWh × tarifaPorKWh

A potência é uma estimativa média durante a impressão.
O consumo real pode variar com temperaturas, materiais,
ambiente e comportamento da máquina.

## 5. Desgaste

custoDesgaste = tempoEmHoras × desgastePorHora

O desgaste por hora é uma provisão definida pela pessoa para
depreciação e manutenção da impressora.

O aplicativo não calcula automaticamente essa taxa nesta versão.

## 6. Embalagem

custoEmbalagem = valor informado

Quando não houver embalagem, considerar zero.

## 7. Custo base

custoBase =
custoFilamento
+ custoEnergia
+ custoDesgaste
+ custoEmbalagem

## 8. Margem de lucro

A margem representa a parcela do preço de venda que permanece
como lucro após descontar o custo base e a comissão.

margem = percentualDeMargem / 100

Não confundir margem com acréscimo sobre o custo.

Exemplo sem comissão:
- Custo base: R$ 80,00.
- Margem desejada: 20%.
- Preço de venda: R$ 100,00.
- Lucro: R$ 20,00.

Acrescentar 20% ao custo produziria R$ 96,00 e não atenderia
à margem de 20% sobre a venda.

## 9. Comissão

comissao = percentualDeComissao / 100

A comissão incide sobre o preço de venda.

Quando a opção estiver desativada, considerar comissão zero,
mesmo que exista um percentual anteriormente preenchido.

## 10. Preço de venda

precoDeVenda = custoBase / (1 - margem - comissao)

Condições:
- Margem maior ou igual a zero.
- Comissão maior ou igual a zero.
- Margem + comissão inferior a 1.

Uma soma igual ou superior a 100% é inválida.

## 11. Lucro estimado

valorComissao = precoDeVenda × comissao

lucroEstimado = precoDeVenda - custoBase - valorComissao

O lucro considera somente os custos incluídos no cálculo.

## 12. Precisão e arredondamento

A implementação deverá usar representação decimal adequada
ou inteiros com escala definida para os valores monetários.

Não utilizar Float ou Double como representação do dinheiro
nas regras de negócio.

Política inicial:
1. Calcular cada componente sem arredondar etapas intermediárias.
2. Arredondar cada custo final para centavos usando HALF_UP.
3. Somar esses componentes para obter o custo base.
4. Calcular o preço e arredondá-lo para cima até o centavo.
5. Calcular a comissão sobre o preço já arredondado.
6. Arredondar a comissão para centavos usando HALF_UP.
7. Obter o lucro por subtração dos valores finais.

HALF_UP:
Quando o valor estiver exatamente no meio, arredondar para cima.
Exemplo: R$ 1,235 passa a R$ 1,24.

Essa política mantém a igualdade entre os valores exibidos:
preço de venda = custo base + comissão + lucro.

A margem efetiva pode diferir ligeiramente da margem desejada
por causa do arredondamento.

margemEfetiva = (lucroEstimado / precoDeVenda) × 100

Se o preço for zero, a margem efetiva será apresentada como
"Não se aplica", evitando divisão por zero.

## 13. Exemplo de referência

Entradas:
- Peso: 100 g.
- Filamento: R$ 100,00/kg.
- Duração: 5 horas.
- Potência média: 150 W.
- Energia: R$ 1,00/kWh.
- Desgaste: R$ 1,00/h.
- Embalagem: R$ 2,00.
- Margem: 20%.
- Comissão: 10%.

Custos:
- Filamento: R$ 10,00.
- Energia: R$ 0,75.
- Desgaste: R$ 5,00.
- Embalagem: R$ 2,00.
- Custo base: R$ 17,75.

Preço antes do arredondamento:
17,75 / (1 - 0,20 - 0,10) = 25,357142...

Resultado:
- Preço sugerido: R$ 25,36.
- Comissão: R$ 2,54.
- Lucro estimado: R$ 5,07.

Conferência:
17,75 + 2,54 + 5,07 = 25,36.

A potência deste exemplo é apenas um dado de teste,
não uma especificação de um modelo de impressora.

## 14. Cenários obrigatórios de teste

- Cálculo completo do exemplo de referência.
- Cálculo sem embalagem e sem comissão.
- Conversão de horas e minutos.
- Margem zero.
- Comissão ativada e desativada.
- Soma de margem e comissão igual ou superior a 100%.
- Peso ou duração igual a zero.
- Valores negativos.
- Custos com frações de centavo.
- Custo base igual a zero.
- Igualdade entre preço, custos, comissão e lucro.


---

## 15. Implementação inicial do motor de cálculo

**Data:** 16/10/2026  
**Status:** implementação inicial concluída, em validação.

### Arquitetura implementada

A lógica financeira está localizada na camada de domínio,
independente de componentes Android ou Compose.

**Modelos criados:**

- Money.kt
- MoneyRate.kt
- Printer.kt
- PrintCalculationInput.kt
- PrintCalculationResult.kt

**Casos de uso e operações:**

- FixedPointMath.kt
- CalculatePrintCostUseCase.kt

### Precisão monetária

A implementação utiliza aritmética inteira com Long.

- Valores monetários finais são representados em centavos.
- Valores unitários permitem quatro casas decimais.
- Percentuais são representados em pontos-base.
- Operações monetárias não utilizam Float ou Double.
- Arredondamento HALF_UP nos custos e na comissão.
- Arredondamento para cima no preço sugerido.
- Proteção contra estouros numéricos nas operações.

### Funcionalidades implementadas

- Cálculo do custo do filamento.
- Cálculo do consumo de energia.
- Cálculo do desgaste da impressora.
- Inclusão do custo de embalagem.
- Cálculo do custo base.
- Aplicação da margem de lucro.
- Aplicação opcional da comissão.
- Cálculo do preço de venda sugerido.
- Cálculo do lucro estimado.
- Cálculo da margem efetiva.
- Validação de entradas numéricas.

### Testes automatizados

Classes:

- FixedPointMathTest.kt
- CalculatePrintCostUseCaseTest.kt

Cenários cobertos incluem:

- Arredondamento HALF_UP.
- Arredondamento para cima.
- Detecção de estouros numéricos.
- Divisão por zero.
- Exemplo financeiro de referência.
- Conferência da composição do preço.
- Comissão ativada e desativada.
- Validação de margem e comissão.
- Duração de impressão com minutos.
- Custos com frações de centavo.
- Margem zero.
- Custo base zero.
- Rejeição de valores negativos.

### Validação

Comando executado:

`./gradlew :shared:testAndroidHostTest`

Resultado observado:

**BUILD SUCCESSFUL**

### Limitações atuais

A implementação ainda precisa de cobertura adicional
para valores extremos e condições de contorno.

O motor de cálculo ainda não está conectado à interface.

As próximas etapas incluem:

- Ampliação dos testes de domínio.
- Implementação do formulário.
- Conversão e validação dos valores digitados.
- Integração com a tela de resultados.
- Testes do fluxo completo no Android.
