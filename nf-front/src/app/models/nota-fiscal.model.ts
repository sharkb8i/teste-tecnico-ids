import { Column } from "./column.model"
import { Endereco } from "./endereco.model";
import { ItemNotaFiscal } from "./item-nota-fiscal.model";

export interface NotaFiscal {
  id?: string;
  codFornecedor: string;
  valorTotal: number;
  endereco: Endereco;
  enderecoFormatado?: string;
  itens: ItemNotaFiscal[];
  numeroNota?: number;
  fornecedor_id?: string;
  dataEmissao?: string;
  dataExclusao?: string;
}

export let colsNotaFiscal: Array<Column> = [
  new Column(0, 'id', 'ID', '', '', 8),
  new Column(1, 'numeroNota', 'Número da Nota', '', 'Busca por No', 8),
  new Column(2, 'valorTotal', 'Valor Total', '', 'Busca por Valor', 8),
  new Column(3, 'enderecoFormatado', 'Endereço', '', 'Busca por Endereço', 20),
  new Column(4, 'codFornecedor', 'Cod. Fornecedor', '', 'Busca por Fornecedor', 8),
  new Column(5, 'dataEmissao', 'Data de Emissão', '', 'Busca por Data Emissão', 8),
  new Column(6, 'dataExclusao', 'Data de Exclusão', '', 'Busca por Data Exclusão', 8),
  new Column(7, 'acoes', 'Ações', '', '', 10),
]