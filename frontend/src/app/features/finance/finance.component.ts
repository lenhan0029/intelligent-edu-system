import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-finance',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './finance.component.html',
  styleUrls: ['./finance.component.css']
})
export class FinanceComponent implements OnInit {
  tuitions: any[] = [];
  loading = true;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchFinance();
  }

  fetchFinance() {
    this.http.get<any[]>('http://localhost:8080/api/finance').subscribe({
      next: (data) => {
        this.tuitions = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching finance data', err);
        this.loading = false;
      }
    });
  }

  getTotalAmount() {
    return this.tuitions.reduce((sum, item) => sum + (item.amount || 0), 0);
  }
}
