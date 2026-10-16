# PrintWorth — Arquitetura

Status: arquitetura inicial; implementação em andamento.

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

A solução de persistência local será definida e documentada
na implementação do cadastro de impressoras.

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

### 15.4 Comportamento atual

O botão "Iniciar cálculo" apresenta um AlertDialog
temporário.

O formulário, a navegação e os cálculos ainda não
foram implementados.

O diálogo será substituído pela navegação para a
calculadora quando essa funcionalidade estiver pronta.

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

### 17.3 Resultado

A interface inicial e o sistema de temas foram
validados no dispositivo Android.

A implementação está preparada para receber
a navegação e as telas da calculadora.

---

## 18. Próximas implementações

- [ ] Modelos da camada de domínio.
- [ ] Representação monetária com precisão adequada.
- [ ] Caso de uso para cálculo de custos e preços.
- [ ] Testes unitários das regras de negócio.
- [ ] Formulário de dados da impressão.
- [ ] Tela de resultados.
- [ ] Integração do botão "Iniciar cálculo".
- [ ] Cadastro e persistência de impressoras.
- [ ] Testes de acessibilidade e diferentes tamanhos de tela.

A implementação seguirá as regras definidas em
`docs/calculation-rules.md` e os requisitos documentados
em `docs/requirements.md`.
