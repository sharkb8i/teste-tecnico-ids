import { Routes } from '@angular/router';

import { ProdutoComponent } from './produto/produto.component';
import { FornecedorComponent } from './fornecedor/fornecedor.component';
import { NotaFiscalComponent } from './nota-fiscal/nota-fiscal.component';
import { HomeComponent } from './home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'produtos', component: ProdutoComponent },
  { path: 'fornecedores', component: FornecedorComponent },
  { path: 'notas-fiscais', component: NotaFiscalComponent },
  { path: '**', redirectTo: '' }
];