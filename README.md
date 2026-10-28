

<div align="center">

![Banner oficial PrintWorth](assets/branding/printworth-banner.png)

![Android](https://img.shields.io/badge/ANDROID-PLATAFORMA-12343B?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/KOTLIN-MULTIPLATFORM-007F73?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/COMPOSE-MULTIPLATFORM-12343B?style=for-the-badge)
![Material 3](https://img.shields.io/badge/MATERIAL-3-007F73?style=for-the-badge)

![Status](https://img.shields.io/badge/STATUS-EM_DESENVOLVIMENTO-007F73?style=for-the-badge)

**Conheça seus custos. Valorize suas impressões.**

</div>

---

## 📱 Sobre o projeto

O **PrintWorth** é um aplicativo Android desenvolvido em **Kotlin**, utilizando **Kotlin Multiplatform**, **Compose Multiplatform** e **Material 3**, para auxiliar profissionais, empreendedores e entusiastas da impressão 3D.

O objetivo do aplicativo é oferecer uma ferramenta simples, precisa e intuitiva para calcular o custo de fabricação de peças impressas em 3D e sugerir um preço de venda, considerando despesas de produção, margem de lucro e comissão opcional.

O projeto está sendo desenvolvido como parte do meu **portfólio profissional em desenvolvimento Android**, aplicando conceitos de arquitetura de software, gerenciamento de estado, persistência local, testes e documentação técnica.

O **PrintWorth nasceu de uma necessidade real**: minha irmã trabalha com impressão 3D e precisava de uma ferramenta prática para calcular seus custos de produção e definir preços de venda. A partir dessa necessidade, desenvolvi o aplicativo como um projeto pessoal, transformando um problema cotidiano em uma solução tecnológica.

Além de atender a essa demanda, o projeto representa minha evolução no desenvolvimento Android, com foco em boas práticas de arquitetura, precisão financeira, experiência do usuário e testes.

### 🎯 Objetivos

- Facilitar o cálculo dos custos de impressão 3D.
- Ajudar na definição de preços de venda.
- Permitir o gerenciamento de impressoras pessoais.
- Oferecer modelos de impressoras pré-cadastrados.
- Apresentar o lucro estimado de forma transparente.
- Disponibilizar uma experiência intuitiva e acessível.
- Funcionar sem necessidade de conexão com a internet.

> > **Status:** projeto pessoal em evolução. A calculadora financeira, o gerenciamento de impressoras e o suporte a múltiplas moedas (BRL, USD e EUR) estão implementados e foram validados em dispositivo Android.

> **Versão de testes mais recente:** PrintWorth **1.1** (`versionCode 2`). O APK foi gerado, assinado digitalmente e teve sua integridade verificada. A disponibilização pública e os testes adicionais de distribuição permanecem em andamento.

---


## 🎬 Demonstração do aplicativo

Conheça o **PrintWorth** em funcionamento!

O vídeo apresenta as principais funcionalidades do
aplicativo, incluindo o cadastro de impressoras 3D,
o cálculo dos custos de produção e a definição
do preço de venda.


### O que será apresentado

1. **Tela inicial:** apresentação do aplicativo e acesso à calculadora.
2. **Etapa Impressão:** preenchimento dos dados da peça, filamento e tempo de impressão.
3. **Seleção de impressora:** escolha entre modelos do catálogo e impressoras pessoais.
4. **Gerenciamento de impressoras:** cadastro, edição e exclusão.
5. **Etapa Custos:** preenchimento das despesas e definição do método de precificação.
6. **Resultado:** visualização do preço sugerido, lucro estimado e detalhamento dos custos.
7. **Edição do cálculo:** retorno ao formulário preservando os dados preenchidos.
8. **Múltiplas moedas:** seleção de Real, Dólar e Euro, com cálculo e apresentação dos resultados na moeda escolhida.

### ▶️ Assista à demonstração

[🎥 Assistir ao vídeo demonstrativo do PrintWorth](https://github.com/user-attachments/assets/0efe092d-5ad6-45ca-b7d4-3849ff61aa2f)


---

## ✨ Funcionalidades

### 🧵 Custos de produção

- [x] Cálculo do custo do filamento.
- [x] Cálculo do consumo de energia elétrica.
- [x] Cálculo do desgaste da impressora.
- [x] Inclusão opcional de custo de embalagem.
- [x] Cálculo do custo total de produção da peça.
- [x] Validação dos valores informados.

### 💰 Precificação

- [x] Lucro percentual sobre o custo (Markup).
- [x] Margem percentual sobre o preço de venda.
- [x] Inclusão opcional de comissão do ponto de venda.
- [x] Cálculo do preço de venda sugerido.
- [x] Cálculo do lucro estimado.
- [x] Cálculo da margem efetiva.
- [x] Detalhamento individual dos custos.
- [x] Ajuda contextual para os métodos de precificação.


### 🌍 Suporte a múltiplas moedas

- [x] Seleção de Real brasileiro (BRL).
- [x] Seleção de Dólar americano (USD).
- [x] Seleção de Euro (EUR).
- [x] Formatação monetária conforme a moeda escolhida.
- [x] Cálculo de custos, preço de venda e lucro na mesma moeda.
- [x] Confirmação antes de alterar a moeda.
- [x] Limpeza dos valores monetários ao trocar de moeda.
- [x] Funcionamento offline, sem conversão cambial.

### 🖨️ Impressoras 3D

- [x] Catálogo offline com 26 modelos FDM.
- [x] Seleção de modelos diretamente na calculadora.
- [x] Cadastro de impressoras personalizadas.
- [x] Edição de impressoras pessoais.
- [x] Exclusão de impressoras pessoais.
- [x] Definição da potência média utilizada.
- [x] Persistência local com AndroidX DataStore.
- [x] Sincronização entre cadastros e calculadora.
- [x] Preservação de ajustes manuais de potência.
- [x] Invalidação da seleção após exclusão.

### 📲 Experiência de utilização

- [x] Interface com Compose Multiplatform e Material 3.
- [x] Tema Light.
- [x] Tema Dark.
- [x] Adaptação automática ao tema do Android.
- [x] Formulário dividido em etapas.
- [x] Tela de resultados.
- [x] Validação dos campos.
- [x] Preservação dos dados ao editar um cálculo.
- [x] Possibilidade de iniciar um novo cálculo.
- [x] Seção educativa "Sobre o PrintWorth".
- [x] Funcionamento offline das funcionalidades implementadas.

### 🔧 Melhorias previstas

- [ ] Ampliação dos testes automatizados.
- [ ] Tratamento completo de falhas de armazenamento.
- [ ] Restauração do formulário após encerramento do processo Android.
- [ ] Revisão completa de acessibilidade.
- [ ] Testes adicionais de desempenho e compatibilidade.
- [ ] Preparação da distribuição pública do aplicativo.

---

## 🌍 Suporte a múltiplas moedas

O PrintWorth permite realizar cálculos de custos
de impressão 3D e formação de preços utilizando
três moedas:

| Moeda | Código | Exemplo |
|---|---|---|
| Real brasileiro | BRL | R$ 1.234,56 |
| Dólar americano | USD | US$ 1,234.56 |
| Euro | EUR | € 1.234,56 |

### Como funciona?

Na etapa **Impressão**, o usuário seleciona
a moeda que deseja utilizar.

Todos os custos devem ser informados diretamente
na moeda escolhida.

O aplicativo utiliza essa mesma moeda para
apresentar:

- Custo do filamento.
- Custo de energia elétrica.
- Desgaste da impressora.
- Embalagem.
- Custo total de produção.
- Preço de venda sugerido.
- Comissão do ponto de venda.
- Lucro estimado.

**Importante:** o PrintWorth não realiza
conversão cambial.

### Exemplo de cálculo em euros

Considere uma impressão com os seguintes custos:

| Descrição | Valor |
|---|---:|
| Filamento | € 1,00 |
| Energia | € 6,50 |
| Desgaste da impressora | € 5,00 |
| Embalagem | € 2,00 |
| **Custo total** | **€ 14,50** |

Utilizando o método **Lucro sobre o custo**,
com percentual de 100% e sem comissão:

**Preço sugerido: € 29,00**

**Lucro estimado: € 14,50**

**Margem efetiva sobre a venda: 50%**

As mesmas fórmulas funcionam em BRL e USD.

### Segurança na troca de moeda

Se houver custos preenchidos, o PrintWorth
solicitará confirmação antes de alterar a moeda.

Após a confirmação, os valores monetários
anteriores serão limpos.

Os dados da impressão, como impressora,
peso, tempo, potência e percentuais,
serão preservados.

Isso evita misturar valores de moedas
diferentes em um mesmo cálculo.

### Precisão financeira

O aplicativo utiliza aritmética de precisão
fixa para representar valores monetários,
sem depender de Float ou Double.

A formatação dos resultados é realizada
pelo componente `MoneyFormatter`.

As três moedas funcionam offline,
sem necessidade de consultar taxas de câmbio.

Para conhecer as regras e os testes realizados,
consulte a
[documentação das regras de cálculo](docs/calculation-rules.md).

---


## 🖨️ Catálogo de impressoras

O PrintWorth disponibiliza um catálogo inicial com **26 modelos de impressoras 3D FDM**, organizados por fabricante.

### Fabricantes disponíveis

| Fabricante | Quantidade de modelos |
|---|---:|
| Bambu Lab | 6 |
| Creality | 8 |
| Elegoo | 4 |
| Flashforge | 2 |
| Anycubic | 2 |
| Prusa | 3 |
| Sovol | 1 |
| **Total** | **26** |

Os modelos do catálogo podem ser selecionados diretamente na calculadora, sem necessidade de cadastro prévio.

Também é possível cadastrar impressoras pessoais, inclusive mais de uma unidade do mesmo modelo, com configurações diferentes.

### Potência média de impressão

O aplicativo utiliza a **potência média estimada em watts (W)** para calcular o consumo de energia elétrica.

É importante diferenciar:

- **Potência máxima:** valor máximo declarado para o equipamento em condições específicas.
- **Potência média:** consumo estimado durante uma impressão, utilizado no cálculo financeiro.

#### Exemplo: Bambu Lab A1

| Informação | Valor |
|---|---|
| Modelo | Bambu Lab A1 |
| Potência máxima declarada em 220 V | 1.300 W |
| Referência inicial de potência média para PLA | 95 W |
| Potência utilizada no cálculo | Ajustável |

A referência de 95 W é uma estimativa inicial e não representa uma medição individual de cada equipamento.

O consumo real pode variar conforme o material utilizado, temperaturas, velocidade, ambiente e outras condições de impressão.

Para modelos sem referência de potência média, o usuário deverá informar o valor antes de calcular.

### Gerenciamento de impressoras pessoais

O aplicativo permite:

- Cadastrar impressoras.
- Informar nome e consumo médio.
- Editar configurações salvas.
- Excluir impressoras pessoais.
- Utilizar os cadastros diretamente na calculadora.

As informações são armazenadas localmente com **AndroidX DataStore Preferences**.

### Sincronização inteligente

Quando uma impressora pessoal é alterada, a calculadora reconhece a atualização.

Se o usuário já tiver personalizado manualmente a potência para um cálculo específico, esse valor será preservado.

Quando uma impressora selecionada é excluída, a seleção é invalidada e o aplicativo solicita outra impressora.

Isso evita utilizar dados de um cadastro que não está mais disponível.

Para mais detalhes, consulte:

[Documentação do gerenciamento de impressoras](docs/printers.md).

---

## 🧮 Regras de cálculo

O PrintWorth calcula o custo de produção a partir das informações fornecidas pelo usuário.

As operações monetárias utilizam **aritmética de precisão fixa**, evitando o uso de `Float` ou `Double` para representar dinheiro.

### 1. Custo do filamento

```text
Custo do filamento =
    (Peso em gramas / 1000) × Preço do filamento por kg
```

Exemplo:

```text
Peso: 100 g
Preço do filamento: R$ 100,00/kg

Custo do filamento = R$ 10,00
```

O peso utilizado deve considerar o consumo total atribuído à impressão, incluindo suportes, brim, purga e outros materiais descartados durante o processo.

### 2. Custo de energia

```text
Consumo (kWh) =
    (Potência média em W / 1000) × Tempo em horas

Custo de energia =
    Consumo (kWh) × Tarifa de energia
```

Exemplo:

```text
Potência média: 150 W
Tempo: 5 horas
Tarifa: R$ 1,00/kWh

Consumo = 0,75 kWh
Custo de energia = R$ 0,75
```

### 3. Desgaste da impressora

```text
Custo de desgaste =
    Tempo de impressão × Desgaste por hora
```

Exemplo:

```text
Tempo: 5 horas
Desgaste: R$ 1,00/h

Custo de desgaste = R$ 5,00
```

### 4. Embalagem

O usuário pode informar um custo opcional de embalagem.

Quando não existe custo de embalagem, o valor considerado é zero.

### 5. Custo total de produção

```text
Custo base =
    Custo do filamento
  + Custo de energia
  + Custo de desgaste
  + Custo de embalagem
```

Os componentes monetários são arredondados para centavos conforme as regras do motor de cálculo.

---

## 💰 Métodos de precificação

O PrintWorth oferece dois métodos para calcular o preço de venda.

### Método 1 — Lucro sobre o custo (Markup)

Nesse método, o percentual informado representa o acréscimo desejado sobre o custo base da peça.

**Fórmula sem comissão:**

```text
Preço de venda =
    Custo base × (1 + Lucro desejado / 100)
```

Exemplo:

| Descrição | Valor |
|---|---:|
| Custo base | R$ 20,00 |
| Lucro desejado | 100% |
| Preço sugerido | R$ 40,00 |
| Lucro estimado | R$ 20,00 |

Nesse método, o percentual de lucro pode ser igual ou superior a 100%, respeitando os limites numéricos do aplicativo.

### Método 2 — Margem sobre a venda

Nesse método, o percentual representa a parcela do valor de venda que deverá corresponder ao lucro após os custos considerados.

**Fórmula sem comissão:**

```text
Preço de venda =
    Custo base / (1 - Margem desejada / 100)
```

Exemplo:

| Descrição | Valor |
|---|---:|
| Custo base | R$ 20,00 |
| Margem desejada | 50% |
| Preço sugerido | R$ 40,00 |
| Lucro estimado | R$ 20,00 |

A margem sobre o preço de venda deve ser inferior a 100%.

### Diferença entre lucro sobre o custo e margem sobre a venda

Considere uma peça com:

```text
Custo base: R$ 20,00
Preço de venda: R$ 40,00
Lucro estimado: R$ 20,00
```

O lucro sobre o custo será:

```text
20 / 20 × 100 = 100%
```

A margem sobre a venda será:

```text
20 / 40 × 100 = 50%
```

Os dois percentuais estão corretos, mas representam relações financeiras diferentes.

---

## 🏷️ Comissão opcional

O usuário pode informar uma comissão percentual de um ponto de venda.

A comissão é aplicada sobre o preço final de venda.

### Comissão no método Sobre o custo

```text
Preço de venda =
    [Custo base × (1 + Lucro desejado / 100)]
    / (1 - Comissão / 100)
```

### Comissão no método Sobre a venda

```text
Preço de venda =
    Custo base
    / (1 - Margem desejada / 100 - Comissão / 100)
```

No método Sobre a venda, a soma da margem desejada com a comissão deve ser inferior a 100%.

O aplicativo valida essas condições antes de executar o cálculo.

### Lucro estimado

```text
Valor da comissão =
    Preço de venda × Comissão / 100

Lucro estimado =
    Preço de venda
  - Custo base
  - Valor da comissão
```

O lucro estimado considera somente as despesas informadas no aplicativo.

Ele não representa necessariamente o lucro líquido de uma empresa.

---

## 📊 Exemplo completo de cálculo

Considere uma peça impressa em 3D com os seguintes dados.

Neste exemplo, utilizamos o método **Margem sobre a venda**, com margem desejada de 20% e comissão de 10%.

### Dados informados

| Parâmetro | Valor |
|---|---:|
| Filamento utilizado | 100 g |
| Preço do filamento | R$ 100,00/kg |
| Tempo de impressão | 5 horas |
| Potência média | 150 W |
| Tarifa de energia | R$ 1,00/kWh |
| Desgaste | R$ 1,00/h |
| Embalagem | R$ 2,00 |
| Margem desejada | 20% |
| Comissão | 10% |

### Resultado esperado

| Descrição | Valor |
|---|---:|
| Filamento | R$ 10,00 |
| Energia | R$ 0,75 |
| Desgaste | R$ 5,00 |
| Embalagem | R$ 2,00 |
| **Custo base** | **R$ 17,75** |
| **Preço de venda sugerido** | **R$ 25,36** |
| Comissão | R$ 2,54 |
| **Lucro estimado** | **R$ 5,07** |

### Observações

O resultado é uma estimativa baseada nas informações fornecidas pelo usuário.

Tributos, mão de obra, frete e possíveis perdas ou falhas de impressão não são incluídos automaticamente nesta versão.

Para conhecer as regras de precisão, arredondamento e validação, consulte:

[Documentação das regras de cálculo](docs/calculation-rules.md).

---

## 🎨 Identidade visual

O PrintWorth utiliza uma identidade visual tecnológica, com cores relacionadas à precisão, confiabilidade e inovação.

### Paleta de cores

| Cor | Código | Aplicação |
|---|---|---|
| Azul-petróleo | `#12343B` | Identidade principal |
| Turquesa | `#007F73` | Destaques e botões |
| Branco suave | `#F5F8FA` | Fundo do tema claro |

A interface utiliza **Compose Multiplatform e Material 3**, com adaptação automática entre os temas Light e Dark do Android.

### Identidade da marca

<p align="center">
  <img src="assets/branding/printworth-logo-light.png" alt="Logotipo PrintWorth em fundo claro" width="42%">
  &nbsp;&nbsp;
  <img src="assets/branding/printworth-logo-semfundo.png" alt="Logotipo PrintWorth com fundo transparente" width="42%">
</p>

<p align="center">
  <em>Identidade visual oficial do PrintWorth.</em>
</p>

### Conceito visual

A identidade utiliza um símbolo estilizado em formato de **P**, composto por elementos geométricos em camadas.

O conceito está relacionado à fabricação aditiva, à precisão e à valorização das peças produzidas.

As versões do logotipo e do banner são armazenadas em:

`assets/branding/`

### Temas do aplicativo

**Light Mode**

- Fundo claro.
- Cards com boa diferenciação visual.
- Destaques em turquesa.
- Textos em azul-petróleo.

**Dark Mode**

- Fundo preto.
- Cards escuros.
- Destaques em turquesa.
- Textos claros para melhor contraste.

A preferência visual acompanha o tema configurado no dispositivo Android.

---

## 🛠️ Tecnologias utilizadas

| Tecnologia | Finalidade |
|---|---|
| Kotlin | Linguagem principal |
| Kotlin Multiplatform | Compartilhamento de código |
| Compose Multiplatform | Interface declarativa |
| Material 3 | Componentes de interface |
| AndroidX ViewModel | Gerenciamento de estado e ciclo de vida |
| StateFlow | Estados observáveis |
| Kotlin Coroutines | Operações assíncronas |
| AndroidX DataStore | Persistência local de impressoras |
| Kotlin Test | Testes automatizados |
| Gradle Kotlin DSL | Configuração e build |
| Git | Controle de versão |
| GitHub | Repositório e documentação |

---

## 🏗️ Arquitetura

O PrintWorth utiliza uma arquitetura organizada por responsabilidades, separando interface, gerenciamento de estado, regras de negócio e persistência de dados.

### Camada de apresentação

Responsável por:

- Exibir as telas.
- Receber as interações do usuário.
- Gerenciar os estados do formulário.
- Exibir mensagens de validação.
- Apresentar resultados.
- Gerenciar o fluxo de navegação.

Principais componentes:

- `CalculatorScreen`
- `CalculatorViewModel`
- `CalculatorFormState`
- `ResultScreen`
- `PrinterManagementScreen`
- `PrinterManagementViewModel`
- `AboutScreen`

### Camada de domínio

Responsável por:

- Modelos de negócio.
- Regras de precificação.
- Validação das entradas.
- Cálculo dos custos.
- Política de precisão monetária.
- Catálogo de impressoras.
- Contratos de acesso aos dados.

Principais componentes:

- `Printer`
- `PrinterCatalog`
- `PrinterRepository`
- `PricingMethod`
- `PrintCalculationInput`
- `PrintCalculationResult`
- `CalculatePrintCostUseCase`
- `FixedPointMath`

### Camada de dados

Responsável pela persistência das impressoras pessoais.

Implementação Android:

`AndroidPrinterRepository`

A comunicação com a camada de domínio é realizada pelo contrato `PrinterRepository`.

### Princípios adotados

- Separação de responsabilidades.
- Gerenciamento de estado com StateFlow.
- Interface declarativa.
- Regras financeiras independentes da interface.
- Dependências fornecidas por construtor.
- Validação de entradas.
- Testabilidade.
- Documentação contínua.

---

## 📂 Estrutura do projeto

```text
PrintWorth/
│
├── androidApp/
│   └── Aplicação Android
│
├── shared/
│   └── src/
│       ├── commonMain/
│       │   └── kotlin/
│       │       └── com/dannyrodrygues/printworth/
│       │           ├── designsystem/
│       │           │   └── theme/
│       │           ├── domain/
│       │           │   ├── catalog/
│       │           │   ├── model/
│       │           │   ├── repository/
│       │           │   └── usecase/
│       │           ├── presentation/
│       │           │   ├── about/
│       │           │   ├── calculator/
│       │           │   ├── printer/
│       │           │   └── result/
│       │           └── App.kt
│       │
│       ├── androidMain/
│       │   └── Implementações específicas do Android
│       │
│       └── commonTest/
│           └── Testes automatizados
│
├── docs/
│   ├── architecture.md
│   ├── calculation-rules.md
│   ├── printers.md
│   └── requirements.md
│
├── assets/
│   └── branding/
│       ├── printworth-banner.png
│       ├── printworth-logo-light.png
│       └── printworth-logo-semfundo.png
│
├── gradle/
│
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

> A estrutura apresenta os principais diretórios e componentes do projeto. Arquivos auxiliares e configurações específicas foram omitidos para facilitar a visualização.

---

## 🚀 Como executar o projeto

### Pré-requisitos

- Android Studio instalado.
- Android SDK configurado.
- JDK compatível com o projeto.
- Git instalado.
- Dispositivo Android ou emulador.

### 1. Clonar o repositório

```bash
git clone https://github.com/DannyRodrygues/PrintWorth.git
```

### 2. Acessar a pasta

```bash
cd PrintWorth
```

### 3. Abrir no Android Studio

Abra a pasta do projeto no Android Studio e aguarde a sincronização do Gradle.

### 4. Compilar o aplicativo Android

```bash
./gradlew :androidApp:assembleDebug
```

### 5. Executar os testes

```bash
./gradlew :shared:testAndroidHostTest
```

### 6. Executar no dispositivo

Conecte um dispositivo Android com a depuração USB ativada ou utilize um emulador compatível.

No Android Studio, selecione o dispositivo e execute o aplicativo.

---

## 🧪 Testes e validação

O projeto possui testes automatizados das regras de negócio e validações do formulário.

### Testes automatizados

Entre os componentes testados estão:

- Operações matemáticas de precisão fixa.
- Cálculo de custos de produção.
- Lucro sobre o custo.
- Margem sobre a venda.
- Comissão opcional.
- Conversão de números decimais.
- Validação de entradas.
- Estado da calculadora.
- Catálogo de impressoras.
- Formatação monetária em BRL, USD e EUR.


Comando utilizado:

```bash
./gradlew :shared:testAndroidHostTest
```

### Testes manuais em dispositivo Android

Foram realizados testes de:

- [x] Inicialização do aplicativo.
- [x] Exibição da tela inicial.
- [x] Navegação entre Impressão, Custos e Resultado.
- [x] Seleção de impressoras do catálogo.
- [x] Seleção de impressoras pessoais.
- [x] Cadastro de impressoras.
- [x] Edição de impressoras.
- [x] Exclusão de impressoras.
- [x] Seleção de modelo sem potência predefinida.
- [x] Atualização da potência após edição do cadastro.
- [x] Preservação de potência ajustada manualmente.
- [x] Invalidação da impressora após exclusão.
- [x] Cálculo utilizando impressoras selecionadas.
- [x] Preservação dos dados ao editar o cálculo.
- [x] Seleção de Real, Dólar e Euro.
- [x] Atualização dos símbolos monetários nos campos.
- [x] Validação dos cálculos nas três moedas.
- [x] Apresentação dos resultados na moeda escolhida.
- [x] Limpeza dos custos após troca de moeda.
- [x] Validação visual dos temas Light e Dark.
- [x] Alternância automática conforme o tema do Android.
- [x] Verificações manuais de legibilidade e usabilidade.
- [x] Geração e verificação da assinatura digital do APK 1.1.


### Testes ainda necessários

- [ ] Ampliação dos testes de valores extremos.
- [ ] Cobertura automatizada das falhas de persistência.
- [ ] Testes de acessibilidade.
- [ ] Validação com diferentes tamanhos de tela.
- [ ] Testes completos de restauração de estado.
- [ ] Revisão final de compatibilidade Android.

---

## 📚 Documentação técnica

O PrintWorth possui documentação dedicada para manter a organização do desenvolvimento e registrar as decisões arquiteturais.

| Documento | Conteúdo |
|---|---|
| [Requisitos](docs/requirements.md) | Escopo, funcionalidades e critérios de aceitação |
| [Arquitetura](docs/architecture.md) | Módulos, camadas e responsabilidades |
| [Regras de cálculo](docs/calculation-rules.md) | Fórmulas, precisão monetária e cenários de teste |
| [Impressoras](docs/printers.md) | Catálogo, cadastro, persistência e sincronização |

A documentação será atualizada continuamente conforme as funcionalidades forem desenvolvidas.

---

## 🗺️ Roadmap do desenvolvimento

### ✅ Etapa 1 — Planejamento e estrutura

- [x] Criação do projeto Android.
- [x] Estrutura inicial Kotlin Multiplatform.
- [x] Configuração do Git.
- [x] Publicação no GitHub.
- [x] Documentação inicial de requisitos.
- [x] Documentação da arquitetura.
- [x] Especificação das regras de cálculo.
- [x] Definição da identidade visual.
- [x] Criação dos protótipos iniciais.

### 🚧 Etapa 2 — Desenvolvimento

**Interface e identidade visual**

- [x] Implementação da tela inicial.
- [x] Integração do logotipo oficial.
- [x] Aplicação da identidade visual PrintWorth.
- [x] Implementação do tema Light.
- [x] Implementação do tema Dark.
- [x] Alternância automática conforme o tema do Android.

**Calculadora**

- [x] Implementação dos modelos de domínio.
- [x] Implementação das regras de cálculo.
- [x] Operações monetárias com precisão fixa.
- [x] Testes unitários iniciais.
- [x] Integração do botão "Iniciar cálculo".
- [x] Formulário dividido em duas etapas.
- [x] Desenvolvimento da tela de resultados.
- [x] Navegação entre as etapas.
- [x] Edição preservando os dados preenchidos.
- [x] Métodos de precificação.
- [x] Comissão opcional.
- [x] Ajuda contextual dos métodos financeiros.
- [x] Suporte a múltiplas moedas (BRL, USD e EUR).
- [x] Formatação monetária internacional.
- [x] Proteção contra mistura de moedas.
- [x] Testes do formatador monetário.
- [x] Validação das três moedas em dispositivo Android.


**Gerenciamento de impressoras**

- [x] Catálogo de 26 modelos.
- [x] Integração do catálogo à calculadora.
- [x] Cadastro de impressoras pessoais.
- [x] Persistência local com DataStore.
- [x] Edição e exclusão.
- [x] Sincronização com a calculadora.
- [x] Preservação de ajustes manuais.
- [x] Validação em dispositivo Android.

### 🔄 Etapa 3 — Qualidade e estabilidade

- [x] Testes unitários iniciais.
- [x] Validação inicial das fórmulas.
- [x] Validação dos campos do formulário.
- [x] Testes manuais no Android.
- [ ] Ampliação dos testes de limites numéricos.
- [ ] Tratamento completo de erros de armazenamento.
- [ ] Restauração de estado após encerramento do processo.
- [ ] Revisão completa de acessibilidade.
- [ ] Verificação de desempenho.
- [ ] Revisão final da interface.

### 📦 Etapa 4 — Distribuição

**APK Android**

- [x] Geração do APK de release.
- [x] Criação e utilização da chave de assinatura.
- [x] Assinatura digital do APK.
- [x] Verificação da assinatura APK Signature Scheme v2.
- [x] Geração da versão 1.1 (`versionCode 2`).
- [x] Validação da integridade e identificação da versão do APK.
- [ ] Validação da instalação da versão 1.1 em outro dispositivo.
- [ ] Teste de atualização preservando os dados existentes.

**Documentação e apresentação**

- [x] Documentação dos requisitos.
- [x] Documentação da arquitetura.
- [x] Documentação das regras financeiras.
- [x] Documentação do gerenciamento de impressoras.
- [x] Documentação do suporte a múltiplas moedas.
- [x] Gravação do vídeo demonstrativo atualizado.
- [x] Inclusão do vídeo no README.
- [ ] Documentação de instalação e atualização do APK.
- [ ] Disponibilização pública do APK.
- [ ] Revisão final para distribuição.
---

## 👩‍💻 Desenvolvedora

**Dany Rodrigues**

Desenvolvimento Android com Kotlin e Compose Multiplatform.

GitHub: [@DannyRodrygues](https://github.com/DannyRodrygues)

Projeto desenvolvido para aprendizado, evolução profissional e composição de portfólio em desenvolvimento Android.

---

<div align="center">

### 🖨️ PrintWorth

**Conheça seus custos. Valorize suas impressões.**

Desenvolvido com Kotlin 💚

</div>
