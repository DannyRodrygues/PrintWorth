# PrintWorth — Requisitos

Status: especificação inicial
Plataforma inicial: Android
Ambiente de desenvolvimento: Ubuntu
Tecnologia: Kotlin Multiplatform e Compose Multiplatform

## 1. Objetivo

Ajudar quem trabalha com impressão 3D a estimar o custo de
produção de uma peça e calcular um preço de venda com lucro,
considerando uma comissão opcional de ponto de venda.

O aplicativo funcionará sem conexão com a internet para
cadastro local de impressoras e realização dos cálculos.

## 2. Escopo da primeira versão

- Formulário de dados da impressão.
- Seleção de impressora.
- Cadastro local de impressoras personalizadas.
- Cálculo dos custos de produção.
- Formação do preço de venda.
- Comissão opcional sobre o preço de venda.
- Tela com resultado e detalhamento dos custos.
- Retorno ao formulário preservando os dados preenchidos.

Não fazem parte desta versão:
- Controle de estoque.
- Gestão de clientes ou pontos de venda.
- Controle de pagamentos.
- Login e sincronização em nuvem.
- Leitura de arquivos STL ou G-code.
- Publicação para iOS.

O histórico de cálculos será avaliado em uma etapa posterior.

## 3. Fluxo principal

1. A pessoa abre o formulário.
2. Seleciona ou cadastra uma impressora.
3. Informa os dados da impressão e da venda.
4. Toca em "Calcular".
5. O aplicativo valida os dados.
6. Se houver erro, apresenta uma orientação junto ao campo.
7. Se os dados forem válidos, abre a tela de resultado.
8. A ação "Editar dados" retorna ao formulário preservando
   os valores preenchidos.

## 4. Entradas

| Campo | Unidade | Regra |
|---|---|---|
| Nome da peça | Texto | Opcional |
| Moeda do cálculo | BRL, USD ou EUR | Seleção, padrão BRL |
| Peso do filamento consumido | g | Obrigatório, maior que zero |
| Preço do filamento | Moeda selecionada/kg | Obrigatório, maior ou igual a zero |
| Tempo de impressão | Horas e minutos | Obrigatório, total maior que zero |
| Impressora | Seleção | Obrigatória |
| Potência média utilizada | W | Obrigatória, maior que zero |
| Tarifa de energia | Moeda selecionada/kWh | Obrigatória, maior ou igual a zero |
| Desgaste da impressora | Moeda selecionada/h | Obrigatório, maior ou igual a zero |
| Embalagem | Moeda selecionada | Opcional, padrão zero |
| Método de precificação | Seleção | Sobre o custo (padrão) ou Sobre a venda |
| Percentual desejado | % | Obrigatório, maior ou igual a zero; sujeito às regras do método |
| Aplicar comissão do ponto | Sim/Não | Padrão não |
| Comissão do ponto | % | Obrigatória quando ativada |

O peso e o tempo devem ser informados pela pessoa, normalmente
com base na estimativa do fatiador.

O peso deve incluir suportes, brim, purga e outros consumos
de filamento atribuíveis à impressão.

Os minutos devem estar entre 0 e 59.
No método Sobre o custo, o lucro desejado pode ser igual
ou superior a 100%, respeitando os limites numéricos.

No método Sobre a venda, a soma da margem desejada com
a comissão deve ser inferior a 100%.

A comissão deve ser inferior a 100% nos dois métodos.

## 5. Impressoras

Cada impressora terá:
- Identificador.
- Nome.
- Potência média estimada em watts.

A pessoa poderá cadastrar sua própria impressora quando
o modelo não estiver disponível.

A potência utilizada no cálculo poderá ser ajustada.

A potência nominal ou máxima da fonte não deve ser apresentada
como se fosse o consumo médio durante a impressão.

Modelos pré-cadastrados somente receberão estimativas de
consumo com origem e condições documentadas.

## 6. Resultado

A tela apresentará:
- Preço de venda sugerido.
- Lucro estimado após a comissão.
- Custo do filamento.
- Custo de energia.
- Desgaste da impressora.
- Embalagem, quando houver.
- Custo base.
- Comissão do ponto, quando ativada.
- Margem efetiva após o arredondamento.

O resultado é uma estimativa baseada nos custos informados.
Tributos, mão de obra, frete e falhas de impressão não estão
incluídos automaticamente nesta versão.

## 7. Interface e acessibilidade

- Nome do aplicativo: PrintWorth.
- Azul-petróleo: #12343B.
- Turquesa: #007F73.
- Fundo claro: #F5F8FA.
- Formulário dividido em grupos compreensíveis.
- Campos com rótulos e unidades visíveis.
- Teclado adequado para cada entrada.
- Suporte à vírgula como separador decimal.
- Erros descritos em texto, sem depender apenas de cores.
- Compatibilidade com ampliação de fonte e leitor de tela.
- Conteúdo rolável em telas pequenas.
- Botão "Calcular" ao final do formulário.

## 8. Critérios de aceitação

- Dados válidos produzem o resultado especificado nas regras
  de cálculo.
- Entradas inválidas não abrem a tela de resultado.
- Campos opcionais desativados não geram custos.
- A comissão é calculada sobre o preço de venda.
- O retorno para editar mantém os dados preenchidos.
- Uma impressora cadastrada permanece disponível após
  fechar e abrir o aplicativo.
- A rotação da tela preserva o formulário.
- O cálculo funciona sem conexão com a internet.
- Os valores exibidos permitem conferir:
  preço = custo base + comissão + lucro.

## 9. Distribuição

A distribuição inicial deverá respeitar o requisito de
não pagar pela publicação.

O plano inicial é disponibilizar um APK assinado para
instalação direta. O processo de assinatura, atualização
e proteção da chave será documentado antes da distribuição.


---

## 10. Métodos de precificação

O PrintWorth disponibiliza dois métodos para
formação do preço de venda.

### 10.1 Lucro sobre o custo — COST_MARKUP

- É o método selecionado por padrão.
- Aplica um acréscimo percentual sobre o custo base.
- Aceita percentuais de 100%, 200% ou superiores,
  dentro dos limites numéricos suportados.
- Compensa a comissão quando estiver ativada.

Exemplo sem comissão:

- Custo base: R$ 20,00.
- Lucro desejado: 100%.
- Preço sugerido: R$ 40,00.
- Lucro estimado: R$ 20,00.

### 10.2 Margem sobre a venda — SALE_MARGIN

- Define a parcela desejada do preço de venda
  que permanece após custos e comissão.
- A margem deve ser inferior a 100%.
- Margem e comissão somadas devem ser inferiores
  a 100%.

Exemplo sem comissão:

- Custo base: R$ 20,00.
- Margem desejada: 50%.
- Preço sugerido: R$ 40,00.
- Lucro estimado: R$ 20,00.

### 10.3 Seleção do método

O formulário deve permitir alternar entre:

- Sobre o custo.
- Sobre a venda.

A alteração do método deve preservar os demais
dados do formulário e invalidar o resultado anterior.

O rótulo do percentual deve mudar conforme
o método selecionado.

Entradas incompatíveis com o método escolhido
devem apresentar mensagens de validação.

## 11. Seção educativa — Sobre o PrintWorth

O aplicativo disponibiliza uma tela educativa
e informativa acessível pela tela inicial.

### Conteúdo

- Objetivo e funcionamento da calculadora.
- Explicação do lucro sobre o custo.
- Explicação da margem sobre a venda.
- Exemplos comparativos de precificação.
- Funcionamento da comissão.
- Custos considerados no cálculo.
- Limitações dos resultados apresentados.
- Informações sobre o projeto.
- Identificação e contato da desenvolvedora.

### Ajuda contextual

A etapa Custos e preço deve apresentar um ícone
de ajuda junto à seleção dos métodos.

Ao tocar no ícone, o usuário deve visualizar
uma explicação dos dois métodos de precificação.

A ajuda não deve apagar ou modificar os
dados preenchidos no formulário.

### Contato

A seção Sobre o projeto deve apresentar:

- Desenvolvedora: Daniella Rodrigues.
- Contato: devdanyoliver@gmail.com.

O endereço deve ser clicável e utilizar um
link mailto para abrir um aplicativo de e-mail
compatível, sem enviar mensagens automaticamente.

## 12. Fluxo de três etapas

O processo de cálculo possui três etapas visuais.

### Etapa 1 — Impressão

- Nome da peça.
- Seleção da impressora.
- Peso do filamento.
- Preço do filamento.
- Tempo de impressão.

### Etapa 2 — Custos

- Potência média utilizada.
- Tarifa de energia.
- Desgaste da impressora.
- Embalagem.
- Método de precificação.
- Percentual desejado.
- Comissão opcional.

### Etapa 3 — Resultado

- Preço de venda sugerido.
- Lucro estimado.
- Margem efetiva sobre a venda.
- Detalhamento dos custos.
- Comissão.
- Ação para editar os dados.
- Ação para iniciar novo cálculo.

O retorno para edição deve preservar
os dados anteriormente preenchidos.

A ação Novo cálculo deve limpar o formulário.

O indicador visual deve identificar a etapa atual
e as etapas já concluídas.

## 13. Limitações e funcionalidades futuras

Funcionalidades ainda previstas:

- Cadastro de impressoras personalizadas.
- Persistência local das impressoras.
- Ampliação dos testes de limites numéricos.
- Validação de acessibilidade.
- Revisão completa do tema Light.
- Distribuição da versão Android.

O salvamento de cálculos permanece como
funcionalidade futura, sujeito à definição
de escopo e persistência.


---

## 14. Suporte a múltiplas moedas

**Status:** Implementado e validado no Android.

O PrintWorth permite calcular custos de produção,
preços de venda e lucro estimado utilizando
três moedas.

### 14.1 Moedas disponíveis

| Código | Moeda | Símbolo |
|---|---|---|
| BRL | Real brasileiro | R$ |
| USD | Dólar americano | US$ |
| EUR | Euro | € |

A moeda padrão do aplicativo é o Real brasileiro (BRL).

O usuário pode selecionar a moeda na primeira
etapa da calculadora, denominada Impressão.

### 14.2 Regra principal

Todos os valores monetários devem ser informados
diretamente na moeda selecionada pelo usuário.

O PrintWorth não realiza conversão cambial.

Não são utilizadas taxas de câmbio ou consultas
externas para executar os cálculos.

Exemplo:

Se o usuário selecionar EUR e informar
€ 10,00 como preço do filamento por quilograma,
o aplicativo utilizará esse valor diretamente
em euros.

Para um consumo de 100 gramas:

Custo do filamento = € 1,00.

O custo total, o preço sugerido, a comissão
e o lucro estimado também serão apresentados
em euros.

### 14.3 Campos afetados pela moeda

A moeda selecionada deve ser utilizada em:

- Preço do filamento por quilograma.
- Tarifa de energia por kWh.
- Desgaste da impressora por hora.
- Custo de embalagem.
- Custo total de produção.
- Preço de venda sugerido.
- Comissão calculada.
- Lucro estimado.
- Detalhamento dos custos.

Peso, tempo, potência elétrica e percentuais
não dependem da moeda selecionada.

### 14.4 Alteração da moeda

O usuário pode alterar a moeda durante
o preenchimento da calculadora.

Quando existem valores monetários preenchidos,
o aplicativo solicita confirmação antes
de realizar a alteração.

Após a confirmação:

- A nova moeda é selecionada.
- Os valores monetários anteriores são limpos.
- O nome da peça é preservado.
- A impressora selecionada é preservada.
- O peso do filamento é preservado.
- O tempo de impressão é preservado.
- A potência média é preservada.
- Os percentuais de lucro e comissão são preservados.
- O método de precificação é preservado.
- Um resultado anterior é invalidado.

Se o usuário cancelar a alteração,
a moeda e os dados preenchidos permanecem
inalterados.

Essa proteção evita misturar valores
de moedas diferentes no mesmo cálculo.

### 14.5 Formatação monetária

Os valores financeiros são apresentados
conforme a moeda selecionada.

Exemplos:

| Moeda | Valor apresentado |
|---|---|
| BRL | R$ 1.234,56 |
| USD | US$ 1,234.56 |
| EUR | € 1.234,56 |

Os valores apresentados são exemplos
de formatação, não de conversão cambial.

Os valores financeiros continuam sendo
calculados com aritmética de precisão fixa,
sem utilização de Float ou Double
para representar dinheiro.

### 14.6 Métodos de precificação

Os dois métodos existentes continuam disponíveis:

- Lucro sobre o custo (COST_MARKUP).
- Margem sobre a venda (SALE_MARGIN).

As fórmulas, as validações de percentuais
e a aplicação da comissão não são alteradas
pela escolha da moeda.

O resultado é apresentado na mesma moeda
utilizada nos custos informados.

### 14.7 Funcionamento offline

A seleção da moeda e os cálculos financeiros
não exigem conexão com a internet.

O aplicativo não depende de serviços
de cotação cambial.

### 14.8 Seção educativa

A tela Sobre o PrintWorth possui um assunto
expansível denominado "Moedas disponíveis".

Essa seção explica:

- As três moedas suportadas.
- A necessidade de informar os custos
  na moeda escolhida.
- A ausência de conversão cambial.
- O comportamento da troca de moeda.

### 14.9 Validação

A funcionalidade foi testada manualmente
em dispositivo Android.

Cenários validados:

- [x] Seleção do Real brasileiro (BRL).
- [x] Seleção do Dólar americano (USD).
- [x] Seleção do Euro (EUR).
- [x] Atualização dos símbolos nos campos.
- [x] Exibição dos resultados na moeda selecionada.
- [x] Cálculo correto em euros.
- [x] Cálculos conferidos nas três moedas.
- [x] Limpeza dos valores monetários ao trocar a moeda.
- [x] Funcionamento da seleção na calculadora.

A ampliação da cobertura de testes automatizados
e de cenários extremos permanece prevista
para a fase de qualidade.