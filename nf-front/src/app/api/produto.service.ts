import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { Produto } from "../models/produto.model";
import { RespostaListagem } from "../models/resposta-listagem.model";
import { environment } from "../../environments/environment";

@Injectable({ providedIn: 'root' })
export class ProdutoService {
  private apiUrl = environment.apiUrl + "/api/produtos";

  constructor(private http: HttpClient) {}

  getProdutos(): Observable<RespostaListagem> {
    return this.http.get<RespostaListagem>(this.apiUrl);
  }

  addProduto(produto: Produto) {
    return this.http.post(this.apiUrl, produto);
  }

  updateProduto(produto: Produto) {
    return this.http.patch(`${this.apiUrl}/${produto.id}`, produto);
  }

  deleteProduto(id: string) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}