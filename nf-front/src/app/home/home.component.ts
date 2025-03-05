import { HttpClientModule } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [
    HttpClientModule
  ]
})
export class HomeComponent implements OnInit {
  constructor() {}

  ngOnInit() {
  }
}