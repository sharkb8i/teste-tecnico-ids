import { Column } from "./column.model"
import { Produto } from "./produto.model";

export interface ItemNotaFiscal {
  id?: string;
  codProduto: string;
  valorUnitario: number;
  quantidade: number;
  produto?: Produto;
  nf_id?: string;
  produto_id?: string;
}

export let colsItemNotaFiscal: Array<Column> = [
  new Column(0, 'codProduto', 'Cod. Produto', '', '', 30),
  new Column(1, 'valorUnitario', 'Valor Unitário', '', '', 30),
  new Column(2, 'quantidade', 'Quantidade', '', '', 30),
  new Column(3, 'acoes', 'Ações', '', '', 10),
]