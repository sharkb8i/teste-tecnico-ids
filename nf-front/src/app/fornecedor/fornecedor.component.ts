import { Component, HostListener, OnInit } from "@angular/core";
import { CommonModule, NgFor, NgIf } from "@angular/common";
import { HttpClientModule } from "@angular/common/http";

import { ButtonModule } from "primeng/button";
import { DialogModule } from "primeng/dialog";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { InputMask } from "primeng/inputmask";
import { InputText } from "primeng/inputtext";
import { Message } from "primeng/message";
import { MessageService } from 'primeng/api';
import { SelectModule } from "primeng/select";
import { TableModule } from 'primeng/table';
import { TagModule } from "primeng/tag";

import { colsFornecedor, Fornecedor, situacoes } from "../models/fornecedor.model";
import { FornecedorService } from "../api/fornecedor.service";

@Component({
  selector: 'app-fornecedor',
  standalone: true,
  templateUrl: './fornecedor.component.html',
  styleUrls: ['./fornecedor.component.css'],
  imports: [
    NgIf, NgFor,
    CommonModule,
    ButtonModule,
    DialogModule,
    InputMask,
    InputText,
    Message,
    ReactiveFormsModule,
    SelectModule,
    TableModule,
    TagModule,
    HttpClientModule
  ]
})
export class FornecedorComponent implements OnInit {
  cols = colsFornecedor;
  situacoes = situacoes;
  
  fornecedores: Fornecedor[] = [];
  fornecedorSelecionado: Fornecedor | null = null;
  displayDialog: boolean = false;
  displayDialogConfirmacao: boolean = false;
  showCopyIcon: boolean = true;
  isNew: boolean = false;

  form: FormGroup;

  constructor(private fornecedorService: FornecedorService,
    private toastService: MessageService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      codigo: ['', Validators.required],
      cnpj: ['', [Validators.required, Validators.pattern(/^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/)]],
      razaoSocial: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefone: ['', Validators.required],
      situacao: ['ATIVO']
    });
  }

  ngOnInit() {
    if (!this.fornecedorSelecionado)
      this.limparFornecedorSelecionado();
    this.carregarFornecedores();
  }

  carregarFornecedores(): void {
    this.fornecedorService.getFornecedores()
      .subscribe({
        next: (data) => {
          this.fornecedores = data.dados;
        },
        error: (error) => {
          this.toastErro('Erro ao carregar fornecedores!');
        }
      });
  }

  limparFornecedorSelecionado(): void {
    this.fornecedorSelecionado = { codigo: '', razaoSocial: '', email: '', telefone: '', cnpj: '', situacao: 'ATIVO' };
  }

  novoFornecedor(): void {
    this.limparFornecedorSelecionado();
    this.isNew = true;
    this.displayDialog = true;
  }

  editarFornecedor(fornecedor: Fornecedor): void {
    this.form.patchValue({
      codigo: fornecedor.codigo,
      razaoSocial: fornecedor.razaoSocial,
      email: fornecedor.email,
      telefone: fornecedor.telefone,
      cnpj: fornecedor.cnpj,
      situacao: fornecedor.situacao
    });

    this.fornecedorSelecionado = { ...fornecedor };
    this.isNew = false;
    this.displayDialog = true;
  }

  salvarFornecedor(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const fornecedorData = {
      id: this.fornecedorSelecionado?.id,
      codigo: this.form.get('codigo')?.value,
      razaoSocial: this.form.get('razaoSocial')?.value,
      email: this.form.get('email')?.value,
      telefone: this.form.get('telefone')?.value,
      cnpj: this.form.get('cnpj')?.value,
      situacao: this.form.get('situacao')?.value
    };

    if (this.fornecedorSelecionado) {
      if (this.fornecedorSelecionado.id)
        this.fornecedorService.updateFornecedor(fornecedorData)
          .subscribe({
            next: () => {
              this.carregarFornecedores();
              this.limparFornecedorSelecionado();
              this.isNew = false;
              this.toastSucesso('Fornecedor atualizado com sucesso!');
            },
            error: (res) => {
              const erroMensagem = res.error?.mensagem || 'Erro desconhecido!';
              this.toastErro(erroMensagem);
            }
          });
      else
        this.fornecedorService.addFornecedor(fornecedorData)
          .subscribe({
            next: () => {
              this.carregarFornecedores();
              this.limparFornecedorSelecionado();
              this.isNew = false;
              this.toastSucesso('Fornecedor criado com sucesso!');
            },
            error: (res) => {
              const erroMensagem = res.error?.mensagem || 'Erro desconhecido!';
              this.toastErro(erroMensagem);
            }
          });

      this.displayDialog = false;
    }
  }

  excluirFornecedor(fornecedor: Fornecedor): void {
    this.fornecedorSelecionado = { ...fornecedor };
    this.displayDialogConfirmacao = true;
  }

  confirmarExclusao(): void {
    if (this.fornecedorSelecionado)
      if (this.fornecedorSelecionado.id)
        this.fornecedorService.deleteFornecedor(this.fornecedorSelecionado.id).subscribe({
          next: () => {
            this.carregarFornecedores();
            this.limparFornecedorSelecionado();
            this.displayDialogConfirmacao = false;
            this.toastSucesso('Fornecedor excluído com sucesso!');
          },
          error: (res) => {
            this.displayDialogConfirmacao = false;
            const erroMensagem = res.error?.mensagem || 'Erro desconhecido!';
            this.toastErro(erroMensagem);
          }
        });
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

  emailValido(email: string | undefined): boolean {
    if (!email)
      return false;
    
    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    return emailPattern.test(email);
  }
  
  @HostListener('document:keydown.escape', ['$event'])
  onKeydownHandler(event: KeyboardEvent) {
    if (this.displayDialog)
      this.fecharModal();
  }

  getSituacao(status: string): "success" | "secondary" | "info" | "warn" | "danger" | "contrast" | undefined {
    switch (status) {
      case 'ATIVO': return 'success';
      case 'INATIVO': return 'danger';
      default: return 'info';
    }
  }

  toastSucesso(mensagem: string): void {
    this.toastService.add({ severity:'success', summary: 'Sucesso', detail: mensagem });
  }

  toastErro(mensagem: string): void {
    this.toastService.add({ severity:'error', summary: 'Erro', detail: mensagem });
  }
}