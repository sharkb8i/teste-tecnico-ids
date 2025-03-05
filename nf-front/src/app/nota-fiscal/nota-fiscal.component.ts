import { Component, HostListener, OnInit } from "@angular/core";
import { CommonModule, NgFor, NgIf } from "@angular/common";
import { HttpClientModule } from "@angular/common/http";

import { ButtonModule } from "primeng/button";
import { DialogModule } from "primeng/dialog";
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from "@angular/forms";
import { InputNumber } from "primeng/inputnumber";
import { InputText } from "primeng/inputtext";
import { MessageService } from 'primeng/api';
import { SelectModule } from "primeng/select";
import { TableModule } from 'primeng/table';
import { TagModule } from "primeng/tag";

import { colsNotaFiscal, NotaFiscal } from "../models/nota-fiscal.model";
import { NotaFiscalService } from "../api/nota-fiscal.service";
import { colsItemNotaFiscal, ItemNotaFiscal } from "../models/item-nota-fiscal.model";
import { Endereco } from "../models/endereco.model";

@Component({
  selector: 'app-nota-fiscal',
  standalone: true,
  templateUrl: './nota-fiscal.component.html',
  styleUrls: ['./nota-fiscal.component.css'],
  imports: [
    NgIf, NgFor,
    CommonModule,
    ButtonModule,
    DialogModule,
    DropdownModule,
    FormsModule,
    InputNumber,
    InputText,
    SelectModule,
    TableModule,
    TagModule,
    HttpClientModule
  ]
})
export class NotaFiscalComponent implements OnInit {
  cols = colsNotaFiscal;
  colsItens = colsItemNotaFiscal;

  notasFiscais: NotaFiscal[] = [];
  notaFiscalSelecionada: NotaFiscal | null = null;
  displayDialog: boolean = false;
  displayDialogConfirmacao: boolean = false;
  showCopyIcon: boolean = true;
  isNew: boolean = false;

  constructor(private notaFiscalService: NotaFiscalService,
    private toastService: MessageService) {}

  ngOnInit() {
    if (!this.notaFiscalSelecionada)
      this.limparNotaSelecionada();

    this.carregarNotasFiscais();
  }

  carregarNotasFiscais(): void {
    this.notaFiscalService.getNotasFiscais()
      .subscribe({
        next: (data) => {
          this.notasFiscais = data.dados.map(nota => ({
            ...nota,
            codFornecedor: nota.fornecedor?.codigo ?? null,
            enderecoFormatado: this.formatarEndereco(nota.endereco)
          }));
        },
        error: (error) => {
          this.toastErro('Erro ao carregar notas fiscais!');
        }
      });
  }

  limparNotaSelecionada(): void {
    this.notaFiscalSelecionada = {
      codFornecedor: '',
      valorTotal: 0,
      endereco: {
        logradouro: '', numero: 0, bairro: '', cidade: '', estado: ''
      },
      itens: []
    };
  }

  novaNotaFiscal(): void {
    this.limparNotaSelecionada();
    this.isNew = true;
    this.displayDialog = true;
  }

  editarNotaFiscal(notaFiscal: NotaFiscal): void {
    this.notaFiscalSelecionada = { ...notaFiscal };
    
    this.notaFiscalSelecionada.itens.forEach(item => {
      item.codProduto = item.produto?.codigo ?? '';
    });

    this.displayDialog = true;
  }

  emitirNotaFiscal(notaFiscal: NotaFiscal): void {
    this.notaFiscalSelecionada = { ...notaFiscal };

    if (this.notaFiscalSelecionada)
      if (this.notaFiscalSelecionada.id)
        this.notaFiscalService.emitirNotaFiscal(this.notaFiscalSelecionada.id)
          .subscribe({
            next: () => {
              this.carregarNotasFiscais();
              this.limparNotaSelecionada();
              this.toastSucesso('Nota fiscal emitida com sucesso!');
            },
            error: (res) => {
              const erroMensagem = res.error?.mensagem || 'Erro desconhecido!';
              this.toastErro(erroMensagem);
            }
          });
  }

  salvarNotaFiscal(): void {
    if (this.notaFiscalSelecionada) {
      if (this.notaFiscalSelecionada.id)
        this.notaFiscalService.updateNotaFiscal(this.notaFiscalSelecionada)
          .subscribe({
            next: () => {
              this.carregarNotasFiscais();
              this.limparNotaSelecionada();
              this.isNew = false;
              this.toastSucesso('Nota fiscal atualizada com sucesso!');
            },
            error: (res) => {
              const erroMensagem = res.error?.mensagem || 'Erro desconhecido!';
              this.carregarNotasFiscais();
              this.isNew = false;
              this.toastErro(erroMensagem);
            }
          });
      else
        this.notaFiscalService.addNotaFiscal(this.notaFiscalSelecionada)
          .subscribe({
            next: () => {
              this.carregarNotasFiscais();
              this.limparNotaSelecionada();
              this.isNew = false;
              this.toastSucesso('Nota fiscal criada com sucesso!');
            },
            error: (res) => {
              const erroMensagem = res.error?.mensagem || 'Erro desconhecido!';
              this.isNew = false;
              this.toastErro(erroMensagem);
            }
          });
      
      this.displayDialog = false;
    }
  }

  excluirNotaFiscal(notaFiscal: NotaFiscal): void {
    this.notaFiscalSelecionada = { ...notaFiscal };
    this.displayDialogConfirmacao = true;
  }

  confirmarExclusao(): void {
    if (this.notaFiscalSelecionada)
      if (this.notaFiscalSelecionada.id)
        this.notaFiscalService.deleteNotaFiscal(this.notaFiscalSelecionada.id).subscribe({
          next: () => {
            this.carregarNotasFiscais();
            this.limparNotaSelecionada();
            this.displayDialogConfirmacao = false;
            this.toastSucesso('Nota fiscal excluída com sucesso!');
          },
          error: (res) => {
            this.displayDialogConfirmacao = false;
            const erroMensagem = res.error?.mensagem || 'Erro desconhecido!';
            this.toastErro(erroMensagem);
          }
        });
  }

  adicionarItem(): void {
    this.notaFiscalSelecionada?.itens.push({
      codProduto: '',
      quantidade: 1,
      valorUnitario: 0,
    });
  }

  removerItem(itemNotaFiscal: ItemNotaFiscal): void {
    if (this.notaFiscalSelecionada)
      this.notaFiscalSelecionada.itens = this.notaFiscalSelecionada.itens.filter(
        item => item.codProduto !== itemNotaFiscal.codProduto
      );
  }

  temItens(): boolean {
    if (this.notaFiscalSelecionada)
      return this.notaFiscalSelecionada.itens.length > 0
    return false;
  }

  formatarEndereco(endereco: Endereco): string {
    if (!endereco) return '';

    const logradouro = endereco.logradouro ? `${endereco.logradouro},` : '';
    const numero = endereco.numero ? ` ${endereco.numero}` : '';
    const bairro = endereco.bairro ? ` - ${endereco.bairro}` : '';
    const cidadeEstado = endereco.cidade && endereco.estado ? ` - ${endereco.cidade}/${endereco.estado}` : '';

    return `${logradouro}${numero}${bairro}${cidadeEstado}`.trim();
  }

  copiarAreaCorte(value: string): void {
    const textarea = document.createElement('textarea');
    textarea.value = value;
    document.body.appendChild(textarea);
    textarea.select();
    document.execCommand('copy');
    document.body.removeChild(textarea);
    this.toastSucesso('ID copiado para a área de transferência!');
  }

  fecharModal(): void {
    this.displayDialog = false;
  }

  @HostListener('document:keydown.escape', ['$event'])
  onKeydownHandler(event: KeyboardEvent) {
    if (this.displayDialog)
      this.fecharModal();
  }

  toastSucesso(mensagem: string): void {
    this.toastService.add({ severity:'success', summary: 'Sucesso', detail: mensagem });
  }

  toastErro(mensagem: string): void {
    this.toastService.add({ severity:'error', summary: 'Erro', detail: mensagem });
  }
}