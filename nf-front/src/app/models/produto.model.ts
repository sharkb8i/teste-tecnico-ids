import { Column } from "./column.model"

export interface Produto {
  id?: string;
  codigo: string;
  descricao: string;
  situacao: string;
  dataExclusao?: string | null;
}

export let colsProduto: Array<Column> = [
  new Column(0, 'id', 'ID', '', '', 20),
  new Column(1, 'codigo', 'Código', '', 'Busca por Código', 10),
  new Column(2, 'descricao', 'Descrição', '', 'Busca por Descrição', 40),
  new Column(3, 'situacao', 'Situação', '', 'Busca por Situação', 10),
  new Column(4, 'dataExclusao', 'Data de Exclusão', '', 'Busca por Data ', 10),
  new Column(5, 'acoes', 'Ações', '', '', 10),
]

export let situacoes = [
  { value: 'ATIVO', label: 'ATIVO' },
  { value: 'INATIVO', label: 'INATIVO' }
];