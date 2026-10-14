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
| Peso do filamento consumido | g | Obrigatório, maior que zero |
| Preço do filamento | R$/kg | Obrigatório, maior ou igual a zero |
| Tempo de impressão | Horas e minutos | Obrigatório, total maior que zero |
| Impressora | Seleção | Obrigatória |
| Potência média utilizada | W | Obrigatória, maior que zero |
| Tarifa de energia | R$/kWh | Obrigatória, maior ou igual a zero |
| Desgaste da impressora | R$/h | Obrigatório, maior ou igual a zero |
| Embalagem | R$ | Opcional, padrão zero |
| Margem de lucro desejada | % | Obrigatória, maior ou igual a zero |
| Aplicar comissão do ponto | Sim/Não | Padrão não |
| Comissão do ponto | % | Obrigatória quando ativada |

O peso e o tempo devem ser informados pela pessoa, normalmente
com base na estimativa do fatiador.

O peso deve incluir suportes, brim, purga e outros consumos
de filamento atribuíveis à impressão.

Os minutos devem estar entre 0 e 59.
A soma da margem com a comissão deve ser inferior a 100%.

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