# PrintWorth — Arquitetura

Status: arquitetura implementada e em evolução.
Versão Android: PrintWorth 1.1.
Última revisão: 28/10/2026.

## 1. Objetivo

Separar interface, controle de estado, regras de negócio
e armazenamento para facilitar manutenção e testes.

A estrutura será ampliada conforme as funcionalidades
forem implementadas, evitando abstrações sem necessidade.

## 2. Tecnologias

- Kotlin.
- Kotlin Multiplatform.
- Compose Multiplatform.
- Material 3.
- ViewModel para gerenciamento do estado das telas.
- StateFlow para exposição do estado observável.
- Coroutines para operações assíncronas.
- kotlin.test para testes das regras de negócio.

As versões das dependências ficam centralizadas em:
gradle/libs.versions.toml.

A persistência local das impressoras pessoais
utiliza AndroidX DataStore Preferences,
com implementação específica para Android.

## 3. Módulos

### androidApp

Responsável pela aplicação Android:
- Ponto de entrada Android.
- Manifesto e configurações da aplicação.
- Integrações específicas da plataforma.
- Empacotamento e assinatura.

### shared

Responsável pelo código compartilhável:
- Tema e componentes visuais.
- Telas.
- Gerenciamento de estado.
- Modelos e regras de negócio.
- Contratos necessários ao acesso a dados.

A primeira plataforma atendida é Android.
O uso de KMP não significa que a versão iOS já esteja pronta.

## 4. Organização dos pacotes

Pacote base:
com.dannyrodrygues.printworth

### designsystem.theme

Define cores, tipografia, formas e tema do aplicativo.

Arquivo inicial:
PrintWorthTheme.kt.

### designsystem.components

Contém componentes visuais reutilizados pelas telas.

Componentes exclusivos de uma tela permanecem no pacote
daquela funcionalidade até existir necessidade de reutilização.

### domain.model

Contém os modelos usados pelas regras de negócio.

Exemplos previstos:
- Dados da impressão.
- Impressora.
- Resultado do cálculo.

Não depende de Compose, ViewModel ou APIs Android.

### domain.usecase

Contém as operações de negócio, incluindo o cálculo do
custo e do preço sugerido.

Recebe dados tipados e devolve resultados.
Não lê campos de texto nem formata valores para exibição.

### presentation.calculator

Contém:
- Tela do formulário.
- Estado do formulário.
- ViewModel.
- Tratamento das ações da pessoa.
- Conversão das entradas para os tipos aceitos pelo domínio.
- Mensagens de validação dos campos.

### presentation.result

Contém a apresentação do resultado:
- Preço sugerido.
- Lucro estimado.
- Detalhamento dos custos.
- Ação para retornar à edição.

Um ViewModel exclusivo será criado somente se a tela
precisar de gerenciamento próprio de estado ou operações.

### App.kt

Aplica o tema e conecta o fluxo principal de telas.

Não deve concentrar fórmulas, validações ou armazenamento.

## 5. Fluxo de dados

A interface apresenta um estado e emite ações.

Exemplo:
1. A pessoa altera o peso no formulário.
2. A tela comunica a alteração ao ViewModel.
3. O ViewModel atualiza o estado.
4. A interface apresenta o novo estado.

Ao calcular:
1. A tela envia a ação de calcular.
2. O ViewModel interpreta e valida as entradas.
3. O caso de uso verifica as regras de negócio e calcula.
4. O resultado é disponibilizado para a interface.
5. A aplicação apresenta a tela de resultado.

As fórmulas ficam exclusivamente no domínio.

## 6. Estado das telas

- O ViewModel mantém o estado mutável internamente.
- A interface recebe uma visão somente de leitura.
- O estado será representado por modelos imutáveis.
- Alterações produzirão novas versões do estado.
- A coleta de estado na interface respeitará o ciclo de vida.

O formulário deve manter os dados ao:
- Recriar a tela por rotação.
- Abrir o resultado e retornar para edição.

ViewModel, sozinho, não garante restauração após encerramento
do processo pelo sistema. Essa restauração exige uma estratégia
adicional, a ser definida na implementação do formulário.

## 7. Validação

A apresentação verifica:
- Campos vazios.
- Texto que não representa um número.
- Separadores decimais aceitos.
- Mensagens apropriadas para cada campo.

O domínio verifica:
- Valores e intervalos permitidos.
- Duração e peso maiores que zero.
- Soma válida de margem e comissão.
- Limites numéricos definidos para o cálculo.

A validação da interface não substitui a proteção
das regras de negócio.

Entradas inválidas não devem provocar encerramento do app.

## 8. Precisão monetária

As regras estão descritas em calculation-rules.md.

O domínio não utilizará Float ou Double para representar
dinheiro.

A representação escolhida deverá:
- Funcionar no código comum do KMP.
- Ter precisão e arredondamento explícitos.
- Tratar limites e possíveis estouros numéricos.
- Ser coberta por testes.

java.math.BigDecimal não pode ser usado diretamente
no commonMain, pois é uma API da plataforma JVM.

## 9. Acesso a dados

O cadastro de impressoras exigirá armazenamento local.

Quando essa funcionalidade for implementada:
- O domínio definirá o contrato de acesso necessário.
- A camada de dados implementará esse contrato.
- O ViewModel não acessará o banco diretamente.
- Operações de armazenamento não bloquearão a interface.

Os pacotes de dados serão criados junto com a implementação.

## 10. Injeção de dependências

Inicialmente, as dependências serão fornecidas por construtor.

Exemplo:
O ViewModel receberá o caso de uso de cálculo.
O caso de uso receberá um repositório somente se precisar dele.

Uma biblioteca de injeção será considerada se trouxer
benefício concreto à composição das dependências.

## 11. Testes

### Domínio

Em shared/src/commonTest:
- Fórmulas.
- Conversões de unidades.
- Validações.
- Comissão e margem.
- Precisão e arredondamento.
- Limites numéricos.

### Apresentação

- Atualização do estado.
- Mensagens para entradas inválidas.
- Resultado para entradas válidas.
- Preservação dos dados ao editar.

### Interface Android

- Preenchimento e cálculo.
- Exibição do resultado.
- Retorno ao formulário.
- Uso em telas pequenas e com fonte ampliada.

Os testes serão adicionados junto com as funcionalidades.

## 12. Convenções

- Classes, funções e propriedades com nomes em inglês.
- Interface e documentação inicialmente em português.
- Classes e arquivos em PascalCase.
- Funções e propriedades em camelCase.
- Pacotes em letras minúsculas.
- Textos de interface em recursos de strings.
- Valores de cor definidos no tema.
- Comentários explicam decisões, evitando repetir o código.
- Funções devem ter responsabilidades claras.

## 13. Registro de decisões

Decisões importantes serão registradas em docs/decisions.

Cada registro deverá informar:
- Contexto.
- Alternativas consideradas.
- Decisão.
- Motivo.
- Consequências.

Exemplos:
- Representação monetária.
- Persistência local.
- Navegação.
- Estratégia de restauração do formulário.

## 14. Critério de conclusão de uma funcionalidade

Uma funcionalidade estará concluída quando:
- Atender aos requisitos documentados.
- Tratar entradas inválidas e erros esperados.
- Respeitar a separação de responsabilidades.
- Possuir os testes relevantes para seu comportamento.
- Ser verificada no Android.
- Ter sua documentação atualizada.


---

## 15. Implementação da interface inicial

**Status:** implementada e validada no Android.

**Data:** 16/10/2026.

### 15.1 Objetivo

Disponibilizar a primeira interface funcional do PrintWorth,
aplicando a identidade visual oficial e estabelecendo a
estrutura inicial de apresentação do aplicativo.

### 15.2 Arquivos envolvidos

| Arquivo | Responsabilidade |
|---|---|
| `App.kt` | Composição da tela inicial e interação temporária |
| `PrintWorthTheme.kt` | Configuração dos temas Light e Dark |
| `printworth_icon.png` | Recurso visual oficial do aplicativo |
| `MainActivity.kt` | Ponto de entrada Android que chama `App()` |

O ícone utilizado pela interface está localizado em:

`shared/src/commonMain/composeResources/drawable/printworth_icon.png`

### 15.3 Interface implementada

A tela inicial apresenta:

- Ícone oficial da marca PrintWorth.
- Nome PrintWorth com destaque visual em duas cores.
- Slogan: "Conheça seus custos. Valorize suas impressões."
- Card com apresentação da calculadora.
- Botão "Iniciar cálculo".
- Diálogo temporário informando que a calculadora
  será implementada posteriormente.

A composição utiliza componentes do Compose Multiplatform
e Material 3.

O layout utiliza espaçamento, alinhamento centralizado
e conteúdo rolável para adaptação a diferentes alturas
de tela.


### 15.4 Evolução do comportamento

A tela inicial foi implementada originalmente com
um diálogo temporário no botão "Iniciar cálculo".

Esse diálogo foi substituído pela navegação
para a calculadora.

O fluxo atual permite:

- Abrir a calculadora pela tela inicial.
- Preencher as etapas Impressão e Custos.
- Visualizar o resultado financeiro.
- Retornar à edição preservando os dados.
- Iniciar um novo cálculo.
- Abrir a seção educativa Sobre o PrintWorth.

A identidade visual da tela inicial foi preservada.


---

## 16. Sistema de temas Light e Dark

**Status:** implementado e validado manualmente no Android.

### 16.1 Objetivo

Permitir que a interface do PrintWorth se adapte
automaticamente à preferência visual do sistema Android.

A implementação utiliza o MaterialTheme do Material 3,
centralizando as definições de cores em um único arquivo.

### 16.2 Arquivo responsável

`designsystem/theme/PrintWorthTheme.kt`

O componente `PrintWorthTheme` utiliza:

- `lightColorScheme`
- `darkColorScheme`
- `isSystemInDarkTheme()`
- `MaterialTheme`

O tema ativo é selecionado automaticamente de acordo
com a configuração do dispositivo.

### 16.3 Tema Light

| Elemento | Cor |
|---|---|
| Fundo principal | `#F5F8FA` |
| Card principal | `#FFFFFF` |
| Botão principal | `#007F73` |
| Texto principal | `#12343B` |
| Texto secundário | `#52636A` |

O tema claro mantém a identidade visual original
da marca PrintWorth.

### 16.4 Tema Dark

| Elemento | Cor |
|---|---|
| Fundo principal | `#000000` |
| Card principal | `#0B0B0D` |
| Botão principal | `#0A8F87` |
| Texto principal | `#E8EEF0` |
| Texto secundário | `#96A6AE` |
| Destaque da marca | `#23B7AD` |

A interface Dark preserva o layout visual aprovado,
com fundo preto uniforme e somente o card da
calculadora destacado.

### 16.5 Integração com App.kt

O `App.kt` utiliza:

`MaterialTheme.colorScheme`

Os elementos da tela recebem cores sem depender
de valores fixos definidos diretamente na interface.

Dessa maneira:

- Uma única interface atende aos dois temas.
- A identidade visual permanece consistente.
- A mudança de tema não exige duplicação de telas.
- Novos componentes poderão reutilizar as cores do tema.

### 16.6 Decisão arquitetural

Foi decidido utilizar o tema do próprio sistema
operacional como fonte inicial da preferência visual.

**Motivos:**

- Reduzir configurações desnecessárias.
- Respeitar a preferência do usuário.
- Centralizar a identidade visual.
- Facilitar manutenção e evolução.

Não existe, nesta etapa, uma configuração interna
para escolher manualmente entre Light e Dark.

Essa possibilidade poderá ser avaliada futuramente.

---

## 17. Validação da implementação

### 17.1 Ambiente

| Item | Ambiente |
|---|---|
| Sistema de desenvolvimento | Ubuntu Linux |
| IDE | Android Studio |
| Linguagem | Kotlin |
| Interface | Compose Multiplatform |
| Dispositivo de teste | Xiaomi Redmi Note 9S |
| Plataforma validada | Android |

### 17.2 Testes realizados

- [x] Compilação e instalação do aplicativo.
- [x] Inicialização da tela principal.
- [x] Exibição do logotipo oficial.
- [x] Exibição dos textos e do card principal.
- [x] Validação visual do tema Dark.
- [x] Validação visual do tema Light.
- [x] Alternância de aparência conforme o sistema.
- [x] Preservação do layout nos dois temas.

Os testes acima foram realizados manualmente
em dispositivo físico.

Ainda não foram implementados testes automatizados
para os componentes visuais.

#### 17.3 Resultado

A interface inicial e os temas Light e Dark
foram implementados e validados em dispositivo
Android físico.

A navegação entre as telas principais
está implementada e funcional.

O aplicativo permite:

- Acessar a calculadora pela tela inicial.
- Navegar entre as etapas Impressão e Custos.
- Selecionar impressoras do catálogo.
- Gerenciar impressoras pessoais.
- Calcular os custos de produção.
- Selecionar BRL, USD ou EUR.
- Visualizar os resultados financeiros.
- Retornar para editar os dados.
- Iniciar um novo cálculo.
- Consultar a seção educativa.

Os testes manuais confirmaram o funcionamento
dessas funcionalidades em dispositivo Android.

A ampliação dos testes automatizados
e a revisão de cenários extremos continuam
previstas para as próximas etapas.

---



## 18. Estado atual da implementação

**Revisão:** 28/10/2026
**Versão Android:** 1.1

### Funcionalidades implementadas

- [x] Modelos da camada de domínio.
- [x] Representação monetária com precisão fixa.
- [x] Motor de cálculo financeiro.
- [x] Método de lucro sobre o custo.
- [x] Método de margem sobre a venda.
- [x] Comissão opcional.
- [x] Testes unitários iniciais.
- [x] Conversão de entradas decimais.
- [x] Validação dos campos do formulário.
- [x] ViewModel com StateFlow.
- [x] Formulário dividido em duas etapas.
- [x] Tela de resultados.
- [x] Navegação entre as etapas.
- [x] Edição preservando os dados preenchidos.
- [x] Seção educativa Sobre o PrintWorth.
- [x] Ajuda contextual dos métodos financeiros.
- [x] Contato da desenvolvedora por link mailto.
- [x] Catálogo offline com 26 modelos de impressoras FDM.
- [x] Seleção de impressoras diretamente na calculadora.
- [x] Cadastro de impressoras personalizadas.
- [x] Edição e exclusão de impressoras pessoais.
- [x] Persistência local com AndroidX DataStore.
- [x] Sincronização entre cadastro e calculadora.
- [x] Atualização automática da potência cadastrada.
- [x] Preservação de potência ajustada manualmente.
- [x] Invalidação da seleção após exclusão.
- [x] Testes de sincronização em dispositivo Android.
- [x] Suporte a Real, Dólar e Euro.
- [x] Formatação monetária internacional.
- [x] Confirmação e limpeza dos custos ao trocar de moeda.
- [x] Seção educativa sobre múltiplas moedas.
- [x] Validação dos cálculos nas três moedas.
- [x] Testes automatizados do MoneyFormatter.
- [x] Validação visual dos temas Light e Dark.
- [x] Geração e assinatura digital do APK 1.1.
- [x] Verificação da integridade e versão do APK.

### Funcionalidades pendentes

- [ ] Ampliação dos testes de limites numéricos.
- [ ] Tratamento completo de falhas de persistência.
- [ ] Estratégia de restauração após morte do processo.
- [ ] Testes completos de acessibilidade.
- [ ] Revisão de navegação e experiência do usuário.
- [ ] Verificação de desempenho.
- [ ] Preparação da distribuição pública do APK.
- [ ] Testes adicionais de instalação e atualização.

O salvamento de cálculos permanece como
funcionalidade futura a ser definida.

As funcionalidades de gerenciamento de impressoras
foram validadas manualmente em dispositivo Android.

A documentação específica está disponível em:

`docs/printers.md`

---


## 19. Evolução da arquitetura da aplicação

### 19.1 Modelos de domínio

Pacote:

`domain.model`

Arquivos principais:

- Money.kt
- MoneyRate.kt
- Printer.kt
- PrintCalculationInput.kt
- PrintCalculationResult.kt
- PricingMethod.kt

A enumeração PricingMethod identifica os
métodos de formação do preço:

- COST_MARKUP: lucro sobre o custo.
- SALE_MARGIN: margem sobre a venda.

O método padrão é COST_MARKUP.

### 19.2 Motor financeiro

Pacote:

`domain.usecase`

Arquivos:

- FixedPointMath.kt
- CalculatePrintCostUseCase.kt

O motor:

- Calcula custos de produção.
- Aplica o método de precificação selecionado.
- Considera a comissão opcional.
- Calcula preço sugerido e lucro estimado.
- Calcula a margem efetiva sobre a venda.
- Utiliza aritmética inteira.
- Aplica regras explícitas de arredondamento.

As regras e exemplos estão documentados em:

`docs/calculation-rules.md`

### 19.3 Camada de apresentação

Pacote:

`presentation.calculator`

Arquivos:

- CalculatorFormState.kt
- CalculatorViewModel.kt
- DecimalInputParser.kt
- CalculatorFormValidator.kt
- CalculatorScreen.kt

Responsabilidades:

CalculatorFormState:
Mantém os dados digitados e o método escolhido.

CalculatorViewModel:
Gerencia o estado observável, as ações do
formulário, a validação e o resultado do cálculo.

DecimalInputParser:
Converte entradas decimais para valores
inteiros com escala fixa.

CalculatorFormValidator:
Valida os dados e prepara os modelos de domínio.

CalculatorScreen:
Apresenta as etapas Impressão e Custos,
os campos e a ajuda contextual.

As fórmulas financeiras permanecem no domínio.

### 19.4 Tela de resultados

Pacote:

`presentation.result`

Arquivo:

- ResultScreen.kt

A tela recebe um PrintCalculationResult
e apresenta:

- Preço sugerido.
- Lucro estimado.
- Margem efetiva.
- Detalhamento dos custos.
- Comissão.
- Ação Editar cálculo.
- Ação Novo cálculo.

A tela não executa cálculos financeiros.

### 19.5 Seção educativa

Pacote:

`presentation.about`

Arquivo:

- AboutScreen.kt

Responsabilidades:

- Apresentar o funcionamento da calculadora.
- Explicar os dois métodos de precificação.
- Oferecer exemplos financeiros.
- Explicar a comissão e as limitações.
- Apresentar informações do projeto.
- Disponibilizar o contato da desenvolvedora.

O contato utiliza um link mailto, aberto
por aplicativo de e-mail compatível.

A calculadora também possui ajuda contextual
por meio de um diálogo informativo.

### 19.6 Navegação

O arquivo App.kt aplica o PrintWorthTheme
e conecta as telas principais:

- Home
- Calculator
- Result
- About
- Printers

O formulário apresenta duas etapas internas:

1. Impressão.
2. Custos.

A terceira etapa visual é a tela Resultado.

A navegação inicial utiliza estado Compose
para selecionar a tela apresentada.

Uma solução de navegação mais estruturada
poderá ser considerada conforme o projeto crescer.

### 19.7 Estado e ciclo de vida

O CalculatorViewModel utiliza StateFlow
para expor:

- Dados do formulário.
- Erros individuais dos campos.
- Erros gerais.
- Resultado do cálculo.

O retorno da tela Resultado para a calculadora
preserva os valores digitados.

A restauração integral após encerramento
do processo Android ainda não está garantida
e deverá ser tratada separadamente.



### 19.8 Impressoras

O módulo de gerenciamento de impressoras está
implementado e integrado à calculadora.

### Catálogo de impressoras

O aplicativo possui um catálogo offline contendo
26 modelos de impressoras 3D FDM.

Os modelos podem ser selecionados diretamente
na calculadora, sem necessidade de cadastro.

O catálogo é definido em:

`domain/catalog/PrinterCatalog.kt`

### Impressoras pessoais

O usuário pode cadastrar, editar e excluir
impressoras personalizadas.

Cada cadastro possui:

- Identificador único.
- Nome da impressora.
- Potência média estimada em watts.

Os cadastros são armazenados localmente
utilizando AndroidX DataStore Preferences.

### Organização arquitetural

**Domínio:**

- Printer.kt
- PrinterCatalog.kt
- PrinterRepository.kt

**Camada de dados Android:**

- AndroidPrinterRepository.kt

**Apresentação:**

- PrinterManagementViewModel.kt
- PrinterManagementScreen.kt
- CalculatorViewModel.kt
- CalculatorScreen.kt

**Integração:**

- MainActivity.kt
- App.kt

### Sincronização

O CalculatorViewModel mantém a impressora
selecionada sincronizada com os dados disponíveis.

Quando uma impressora pessoal é editada,
a calculadora reconhece a alteração.

Se a potência foi modificada manualmente
durante o cálculo, o valor personalizado
é preservado.

Quando uma impressora selecionada é excluída,
a seleção é invalidada, exigindo uma nova escolha.

A sincronização evita cálculos com dados
desatualizados e preserva os demais campos
preenchidos pelo usuário.

### Validação

Os testes realizados em dispositivo Android
confirmaram:

- Atualização automática da potência.
- Preservação de ajustes manuais.
- Invalidação após exclusão.
- Seleção de impressoras do catálogo.
- Seleção de impressoras pessoais.

A documentação completa está disponível em:

`docs/printers.md`

### 19.9 Testes e validação

Testes automatizados existentes incluem:

- FixedPointMathTest.kt
- CalculatePrintCostUseCaseTest.kt
- DecimalInputParserTest.kt
- CalculatorFormValidatorTest.kt
- CalculatorViewModelTest.kt
- MoneyFormatterTest.kt

Comando utilizado:

`./gradlew :shared:testAndroidHostTest`

O aplicativo também foi testado manualmente
em dispositivo Android físico.

Foram verificados:

- Navegação pelas três etapas.
- Cálculo sobre o custo.
- Cálculo de margem sobre a venda.
- Tratamento de percentuais inválidos.
- Apresentação dos resultados.
- Retorno para edição.
- Seção Sobre o PrintWorth.
- Abertura do link de contato.
- Ajuda contextual da precificação.


Os temas Light e Dark foram validados em
dispositivo Android físico.

A interface também passou por verificações
manuais de usabilidade e legibilidade.

A cobertura completa de acessibilidade
deve ser documentada conforme os recursos
e cenários efetivamente testados.


---

## 20. Suporte a múltiplas moedas

**Status:** Implementado e validado no Android.

### 20.1 Objetivo

Permitir que o usuário realize cálculos financeiros
utilizando uma das três moedas disponíveis:

- BRL — Real brasileiro (R$).
- USD — Dólar americano (US$).
- EUR — Euro (€).

Todos os custos são informados diretamente
na moeda selecionada.

O PrintWorth não realiza conversão cambial
e não depende de serviços de cotação.

### 20.2 Modelo de domínio

Arquivo:

`domain/model/CalculationCurrency.kt`

A enumeração CalculationCurrency representa
as moedas disponíveis.

Cada moeda possui:

- Código identificador.
- Nome para exibição.
- Símbolo monetário.
- Separador decimal.
- Separador de milhares.

A moeda padrão é BRL.

Os cálculos financeiros continuam utilizando
os modelos Money e MoneyRate.

A representação monetária mantém a estratégia
de precisão fixa com valores inteiros.

Não são utilizados Float ou Double para
representar dinheiro.

### 20.3 Estado do formulário

Arquivo:

`presentation/calculator/CalculatorFormState.kt`

O estado do formulário possui a propriedade:

`currency: CalculationCurrency`

Essa propriedade identifica a moeda utilizada
durante o preenchimento e o cálculo.

O estado inicial utiliza:

`CalculationCurrency.BRL`

A moeda faz parte do estado observável
gerenciado pelo CalculatorViewModel.

### 20.4 Gerenciamento da moeda

Arquivo:

`presentation/calculator/CalculatorViewModel.kt`

O ViewModel possui as funções:

- hasEnteredMonetaryValues()
- onCurrencyChanged()

A primeira identifica se já existem
valores monetários preenchidos.

A segunda altera a moeda selecionada,
sem realizar conversão cambial.

Quando a moeda é alterada, são limpos:

- Preço do filamento.
- Tarifa de energia elétrica.
- Desgaste da impressora.
- Custo de embalagem.

São preservados:

- Nome da peça.
- Impressora selecionada.
- Peso do filamento.
- Tempo de impressão.
- Potência média.
- Método de precificação.
- Percentual de lucro ou margem.
- Configurações de comissão.

Um resultado anterior é invalidado
quando a moeda é alterada.

### 20.5 Interface da calculadora

Arquivo:

`presentation/calculator/CalculatorScreen.kt`

A etapa Impressão apresenta o seletor
da moeda do cálculo.

As opções disponíveis são:

- Real brasileiro (BRL).
- Dólar americano (USD).
- Euro (EUR).

A interface utiliza o símbolo da moeda
selecionada nos campos monetários.

Exemplos:

- R$/kg, US$/kg ou €/kg.
- R$/kWh, US$/kWh ou €/kWh.
- R$/h, US$/h ou €/h.

Se já existirem valores monetários preenchidos,
a interface apresenta um diálogo de confirmação
antes de alterar a moeda.

O usuário pode confirmar a troca e a limpeza
dos custos ou cancelar a operação.

A ajuda contextual de precificação também
apresenta exemplos na moeda selecionada.

### 20.6 Formatação monetária

Arquivo:

`presentation/format/MoneyFormatter.kt`

O MoneyFormatter é responsável por apresentar
os valores financeiros na moeda correta.

Recebe:

- Valor monetário em centavos.
- Moeda selecionada.

Retorna o valor formatado para exibição.

Exemplos:

| Moeda | Formatação |
|---|---|
| BRL | R$ 1.234,56 |
| USD | US$ 1,234.56 |
| EUR | € 1.234,56 |

A formatação não modifica o valor numérico
e não realiza conversão cambial.

O cálculo financeiro permanece independente
da formatação visual.

### 20.7 Integração com o resultado

Arquivos:

- App.kt
- ResultScreen.kt

O App.kt obtém a moeda selecionada
a partir do estado do CalculatorViewModel.

Essa moeda é fornecida à ResultScreen
junto ao resultado financeiro calculado.

A ResultScreen utiliza MoneyFormatter
para exibir:

- Preço de venda sugerido.
- Lucro estimado.
- Custo do filamento.
- Custo de energia.
- Desgaste da impressora.
- Embalagem.
- Custo base.
- Comissão do ponto de venda.

Todos os valores são apresentados
na moeda selecionada.

A ResultScreen não realiza cálculos
financeiros nem conversões de moeda.

### 20.8 Regras de negócio

A seleção da moeda não altera as fórmulas
financeiras existentes.

Continuam disponíveis:

- Lucro sobre o custo (COST_MARKUP).
- Margem sobre a venda (SALE_MARGIN).
- Comissão opcional.

Os valores monetários de um cálculo devem
pertencer à mesma moeda.

A troca de moeda reinicia os campos financeiros
para evitar misturar unidades monetárias.

### 20.9 Funcionamento offline

O suporte a múltiplas moedas funciona
integralmente sem conexão com a internet.

Não são utilizadas APIs de câmbio.

O aplicativo não consulta taxas de conversão.

### 20.10 Testes e validação

Teste automatizado:

`presentation/format/MoneyFormatterTest.kt`

O teste verifica a formatação monetária
das três moedas suportadas.

Comando de validação:

`./gradlew :shared:testAndroidHostTest`

Resultado informado durante o desenvolvimento:

BUILD SUCCESSFUL.

Os testes manuais realizados em dispositivo
Android confirmaram:

- [x] Seleção de BRL.
- [x] Seleção de USD.
- [x] Seleção de EUR.
- [x] Atualização dos símbolos monetários.
- [x] Resultados exibidos na moeda selecionada.
- [x] Aplicação correta da margem de lucro.
- [x] Detalhamento financeiro na mesma moeda.
- [x] Limpeza dos custos após troca de moeda.
- [x] Funcionamento sem conversão cambial.

A ampliação dos testes automatizados
de integração permanece como melhoria futura.

### 20.11 Documentação relacionada

As regras funcionais estão registradas em:

`docs/requirements.md`

As fórmulas financeiras estão documentadas em:

`docs/calculation-rules.md`
