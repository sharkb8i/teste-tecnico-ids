import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { Fornecedor } from "../models/fornecedor.model";
import { RespostaListagem } from "../models/resposta-listagem.model";
import { environment } from "../../environments/environment";

@Injectable({ providedIn: 'root' })
export class FornecedorService {
  private apiUrl = environment.apiUrl + "/api/fornecedores";

  constructor(private http: HttpClient) {}

  getFornecedores(): Observable<RespostaListagem> {
    return this.http.get<RespostaListagem>(this.apiUrl);
  }

  addFornecedor(fornecedor: Fornecedor) {
    return this.http.post(this.apiUrl, fornecedor);
  }

  updateFornecedor(fornecedor: Fornecedor) {
    return this.http.patch(`${this.apiUrl}/${fornecedor.id}`, fornecedor);
  }

  deleteFornecedor(id: string) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}