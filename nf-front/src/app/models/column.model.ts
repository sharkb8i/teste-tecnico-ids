export class Column {
  order: number;
  name: string;
  label: string | undefined;
  shortLabel: string | undefined;
  placeholder: string | undefined;
  width: number;
  align: string;
  hidden: boolean;

  constructor(
    order: number,
    name: string,
    label?: string,
    shortLabel?: string,
    placeholder?: string,
    width?: number,
    align?: string,
    hidden?: boolean
  ) {
    this.order = order
    this.name = name;
    this.label = label ? label : name;
    this.shortLabel = shortLabel;
    this.placeholder = placeholder;
    this.width = width ? width : 200;
    this.align = align ? align : 'left';
    this.hidden = hidden !== undefined ? hidden : false;
  }
}