import { Column } from "./column.model"

export interface Fornecedor {
  id?: string;
  codigo: string;
  razaoSocial: string;
  email: string;
  telefone: string;
  cnpj: string;
  situacao: string;
  dataBaixa?: string;
}

export let colsFornecedor: Array<Column> = [
  new Column(0, 'id', 'ID', '', '', 10),
  new Column(1, 'codigo', 'Código', '', 'Busca por Código', 7),
  new Column(2, 'razaoSocial', 'Razão Social', '', 'Busca por Razão', 25),
  new Column(3, 'email', 'Email', '', 'Busca por Email', 15),
  new Column(4, 'telefone', 'Telefone', '', 'Busca por Telefone', 12),
  new Column(5, 'cnpj', 'CNPJ', '', 'Busca por CNPJ', 10),
  new Column(6, 'situacao', 'Situação', '', 'Busca por Situação', 5),
  new Column(7, 'dataBaixa', 'Data da Baixa', '', 'Busca por Data', 5),
  new Column(8, 'acoes', 'Ações', '', '', 10),
]

export let situacoes = [
  { value: 'ATIVO', label: 'ATIVO' },
  { value: 'BAIXADO', label: 'BAIXADO' },
  { value: 'SUSPENSO', label: 'SUSPENSO' }
];