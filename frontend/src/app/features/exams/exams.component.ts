import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-exams',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './exams.component.html',
  styleUrls: ['./exams.component.css']
})
export class ExamsComponent implements OnInit {
  exams: any[] = [];
  loading = true;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchExams();
  }

  fetchExams() {
    this.http.get<any[]>('http://localhost:8080/api/exams').subscribe({
      next: (data) => {
        this.exams = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching exams', err);
        this.loading = false;
      }
    });
  }
}
