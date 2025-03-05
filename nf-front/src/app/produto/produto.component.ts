import { Component, HostListener, OnInit } from "@angular/core";
import { CommonModule, NgFor, NgIf } from "@angular/common";
import { HttpClientModule } from "@angular/common/http";

import { ButtonModule } from "primeng/button";
import { DialogModule } from "primeng/dialog";
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from "@angular/forms";
import { InputText } from "primeng/inputtext";
import { MessageService } from 'primeng/api';
import { SelectModule } from "primeng/select";
import { TableModule } from 'primeng/table';
import { TagModule } from "primeng/tag";

import { ProdutoService } from "../api/produto.service";
import { colsProduto, Produto, situacoes } from "../models/produto.model";

@Component({
  selector: 'app-produto',
  standalone: true,
  templateUrl: './produto.component.html',
  styleUrls: ['./produto.component.css'],
  imports: [
    NgIf, NgFor,
    CommonModule,
    ButtonModule,
    DialogModule,
    DropdownModule,
    FormsModule,
    InputText,
    SelectModule,
    TableModule,
    TagModule,
    HttpClientModule
  ]
})
export class ProdutoComponent implements OnInit {
  cols = colsProduto;
  situacoes = situacoes;

  produtos: Produto[] = [];
  produtoSelecionado: Produto | null = null;
  displayDialog: boolean = false;
  displayDialogConfirmacao: boolean = false;
  showCopyIcon: boolean = false;
  isNew: boolean = false;

  constructor(private produtoService: ProdutoService,
    private toastService: MessageService) {}

  ngOnInit() {
    if (!this.produtoSelecionado)
      this.limparProdutoSelecionado();
    
    this.carregarProdutos();
  }

  carregarProdutos() {
    this.produtoService.getProdutos()
      .subscribe({
        next: (data) => {
          this.produtos = data.dados;
        },
        error: (error) => {
          this.toastErro('Erro ao carregar produtos!');
        }
      });
  }

  limparProdutoSelecionado(): void {
    this.produtoSelecionado = { codigo: '', descricao: '', situacao: 'ATIVO', dataExclusao: null };
  }

  novoProduto() {
    this.limparProdutoSelecionado();
    this.isNew = true;
    this.displayDialog = true;
  }

  editarProduto(produto: Produto) {
    this.produtoSelecionado = { ...produto };
    this.isNew = false;
    this.displayDialog = true;
  }

  salvarProduto() {
    if (this.produtoSelecionado) {
      if (this.produtoSelecionado.id)
        this.produtoService.updateProduto(this.produtoSelecionado)
          .subscribe({
            next: () => {
              this.carregarProdutos();
              this.limparProdutoSelecionado();
              this.isNew = false;
              this.toastSucesso('Produto atualizado com sucesso!');
            },
            error: (res) => {
              const erroMensagem = res.error?.mensagem || 'Erro desconhecido!';
              this.toastErro(erroMensagem);
            }
          });
      else
        this.produtoService.addProduto(this.produtoSelecionado)
          .subscribe({
            next: () => {
              this.carregarProdutos();
              this.limparProdutoSelecionado();
              this.isNew = false;
              this.toastSucesso('Produto criado com sucesso!');
            },
            error: (res) => {
              const erroMensagem = res.error?.mensagem || 'Erro desconhecido!';
              this.toastErro(erroMensagem);
            }
          });
      
      this.displayDialog = false;
    }
  }

  excluirProduto(produto: Produto) {
    this.produtoSelecionado = { ...produto };
    this.displayDialogConfirmacao = true;
  }

  confirmarExclusao() {
    if (this.produtoSelecionado)
      if (this.produtoSelecionado.id)
        this.produtoService.deleteProduto(this.produtoSelecionado.id).subscribe({
          next: () => {
            this.carregarProdutos();
            this.limparProdutoSelecionado();
            this.displayDialogConfirmacao = false;
            this.toastSucesso('Produto excluído com sucesso!');
          },
          error: (res) => {
            this.displayDialogConfirmacao = false;
            const erroMensagem = res.error?.mensagem || 'Erro desconhecido!';
            this.toastErro(erroMensagem);
          }
        });
  }

  getSituacao(status: string): "success" | "secondary" | "info" | "warn" | "danger" | "contrast" | undefined {
    switch (status) {
      case 'ATIVO': return 'success';
      case 'INATIVO': return 'danger';
      default: return 'info';
    }
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

  toastSucesso(mensagem: string) {
    this.toastService.add({ severity:'success', summary: 'Sucesso', detail: mensagem });
  }

  toastErro(mensagem: string) {
    this.toastService.add({ severity:'error', summary: 'Erro', detail: mensagem });
  }
}