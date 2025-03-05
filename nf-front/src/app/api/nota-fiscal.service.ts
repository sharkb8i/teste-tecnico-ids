import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { NotaFiscal } from "../models/nota-fiscal.model";
import { RespostaListagem } from "../models/resposta-listagem.model";
import { environment } from "../../environments/environment";

@Injectable({ providedIn: 'root' })
export class NotaFiscalService {
  private apiUrl = environment.apiUrl + "/api/notas-fiscais";

  constructor(private http: HttpClient) {}

  getNotasFiscais(): Observable<RespostaListagem> {
    return this.http.get<RespostaListagem>(this.apiUrl);
  }

  emitirNotaFiscal(id: string) {
    return this.http.get(`${this.apiUrl}/emitir/${id}`);
  }

  addNotaFiscal(notaFiscal: NotaFiscal) {
    return this.http.post(this.apiUrl, notaFiscal);
  }

  updateNotaFiscal(notaFiscal: NotaFiscal) {
    return this.http.patch(`${this.apiUrl}/${notaFiscal.id}`, notaFiscal);
  }

  deleteNotaFiscal(id: string) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}