
# PrintWorth — Catálogo e Gerenciamento de Impressoras

**Status:** Implementado e validado no Android  
**Última atualização:** 28/10/2026

## 1. Objetivo

Permitir que o usuário selecione uma impressora 3D
para calcular o custo de produção de uma peça.

O aplicativo disponibiliza modelos pré-cadastrados
e permite cadastrar impressoras pessoais.

A funcionalidade opera sem necessidade de internet.

## 2. Catálogo de impressoras

O catálogo inicial contém 26 modelos FDM,
organizados por fabricante:

- Bambu Lab
- Creality
- Elegoo
- Flashforge
- Anycubic
- Prusa
- Sovol

O catálogo está implementado em:

`domain/catalog/PrinterCatalog.kt`

Os modelos do catálogo podem ser selecionados
diretamente na calculadora, sem cadastro prévio.

## 3. Potência elétrica

O cálculo utiliza a potência média estimada,
expressa em watts (W).

A potência máxima declarada de um equipamento
não deve ser confundida com o consumo médio
durante a impressão.

### Exemplo: Bambu Lab A1

Dados registrados no catálogo:

- Potência máxima declarada: 1.300 W em 220 V.
- Referência inicial de consumo médio: 95 W.

O valor de 95 W é uma estimativa orientativa,
não uma medição individual do equipamento.

O consumo real depende de fatores como
material, temperaturas, velocidade de impressão,
ambiente e fase do processo.

O usuário pode ajustar a potência utilizada
em cada cálculo.

Para modelos sem referência de consumo médio,
o aplicativo solicita que o usuário informe
uma potência positiva.

## 4. Impressoras pessoais

O usuário pode:

- Cadastrar uma impressora.
- Informar seu nome.
- Definir a potência média estimada.
- Editar um cadastro existente.
- Excluir uma impressora.
- Selecionar impressoras pessoais na calculadora.

É permitido cadastrar mais de uma unidade
do mesmo modelo com configurações diferentes.

## 5. Persistência local

As impressoras pessoais são armazenadas
localmente no dispositivo Android.

Tecnologia:

AndroidX DataStore Preferences.

Implementação:

`data/repository/AndroidPrinterRepository.kt`

Contrato:

`domain/repository/PrinterRepository.kt`

O acesso aos dados utiliza Kotlin Flow e
operações assíncronas.

O catálogo fixo não depende do DataStore.

## 6. Arquitetura

### Domínio

`Printer.kt`
Representa uma impressora selecionável.

`PrinterCatalog.kt`
Define os modelos pré-cadastrados.

`PrinterRepository.kt`
Define o contrato de persistência.

### Dados Android

`AndroidPrinterRepository.kt`
Implementa cadastro, edição, exclusão
e observação das impressoras pessoais.

### Apresentação

`PrinterManagementViewModel.kt`
Gerencia a lista de impressoras pessoais,
as operações de gravação e suas mensagens.

`PrinterManagementScreen.kt`
Apresenta o formulário de cadastro
e a lista de impressoras pessoais.

`CalculatorViewModel.kt`
Gerencia a seleção de impressora e sincroniza
alterações realizadas no cadastro.

`CalculatorScreen.kt`
Apresenta o seletor de impressoras
e o campo de potência média.

### Integração

`MainActivity.kt`
Fornece a implementação Android do repositório.

`App.kt`
Conecta o catálogo, as impressoras pessoais,
a calculadora e a tela de gerenciamento.

## 7. Sincronização com a calculadora

### Alteração de impressora pessoal

Se a potência cadastrada for modificada,
a calculadora atualiza o valor automaticamente
quando ele ainda não tiver sido ajustado
manualmente naquele cálculo.

### Ajuste manual

Se o usuário alterou a potência diretamente
na calculadora, esse valor é preservado
durante a sincronização.

### Exclusão

Se uma impressora pessoal selecionada
for excluída, a seleção é invalidada.

O aplicativo solicita outra impressora
e preserva os demais campos do formulário.

### Resultado anterior

Uma alteração relevante na impressora
invalida o resultado calculado anteriormente.

É necessário recalcular para obter
valores consistentes.

## 8. Validações

- Nome obrigatório para impressoras pessoais.
- Nome com até 80 caracteres.
- Potência média positiva para o cálculo.
- Impressora selecionada deve estar disponível.
- Exclusão exige confirmação.
- Alterações de cadastros devem ser sincronizadas.
- Modelos do catálogo não podem ser excluídos
  pela interface de gerenciamento pessoal.

## 9. Testes realizados

### Compilação e testes

Comando utilizado:

`./gradlew :shared:testAndroidHostTest`

Resultado informado: BUILD SUCCESSFUL.

### Testes manuais no Redmi

- [x] Seleção da Bambu Lab A1 do catálogo.
- [x] Seleção de modelo sem potência predefinida.
- [x] Seleção de impressora pessoal.
- [x] Atualização automática da potência cadastrada.
- [x] Preservação de potência ajustada manualmente.
- [x] Exclusão e invalidação da seleção.
- [x] Integração com o formulário de cálculo.

## 10. Pendências de qualidade

O catálogo e o gerenciamento de impressoras
estão implementados e funcionais no Android.

Os testes manuais realizados confirmaram:

- Seleção de impressoras do catálogo.
- Cadastro, edição e exclusão de impressoras pessoais.
- Sincronização com a calculadora.
- Atualização automática da potência cadastrada.
- Preservação de ajustes manuais.
- Invalidação da seleção após exclusão.

### Melhorias e validações adicionais

- [ ] Confirmar a persistência dos cadastros após
  encerramento completo e reabertura do aplicativo,
  caso esse cenário ainda não tenha sido testado.
- [ ] Ampliar testes automatizados de persistência.
- [ ] Validar cenários de falha de leitura e gravação.
- [ ] Ampliar testes específicos de acessibilidade
  da tela de gerenciamento de impressoras.
- [ ] Verificar a interface em diferentes tamanhos
  e resoluções de tela.

Os temas Light e Dark foram validados
manualmente no aplicativo.

A ampliação da cobertura de acessibilidade,
compatibilidade e persistência permanece
prevista para as próximas etapas.

## 11. Decisão de experiência do usuário

A seleção atual permanece como uma lista simples.

Não será acrescentada pesquisa por fabricante
ou modelo nesta etapa.

Essa decisão preserva a interface aprovada
e evita complexidade desnecessária na primeira versão.

A possibilidade de pesquisa poderá ser
reavaliada futuramente.
