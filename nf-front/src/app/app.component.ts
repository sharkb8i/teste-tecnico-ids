import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MessageService } from 'primeng/api';
import { PanelMenuModule } from 'primeng/panelmenu';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    PanelMenuModule,
    ToastModule,
  ],
  providers: [MessageService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'nf-front';
  menuItems = [
    {
      label: 'Home',
      icon: 'pi pi-home',
      routerLink: ['/']
    },
    {
      label: 'Produtos',
      icon: 'pi pi-cog',
      routerLink: ['/produtos']
    },
    {
      label: 'Fornecedores',
      icon: 'pi pi-truck',
      routerLink: ['/fornecedores']
    },
    {
      label: 'Notas Fiscais',
      icon: 'pi pi-file',
      routerLink: ['/notas-fiscais']
    }
  ];
}