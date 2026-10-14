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