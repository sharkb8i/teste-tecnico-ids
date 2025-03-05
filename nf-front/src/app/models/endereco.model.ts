import { Column } from "./column.model"

export interface Endereco {
  id?: string;
  logradouro: string;
  numero: number;
  bairro: string;
  cidade: string;
  estado: string;
}

export let colsEndereco: Array<Column> = [
  new Column(0, 'id', 'ID', '', '', 10),
  new Column(1, 'logradouro', 'Logradouro', '', '', 15),
  new Column(2, 'numero', 'Número', '', '', 10),
  new Column(3, 'bairro', 'Bairro', '', '', 10),
  new Column(4, 'cidade', 'Cidade', '', '', 25),
  new Column(5, 'estado', 'Estado', '', '', 10),
  new Column(6, '', '', '', '', 10),
]