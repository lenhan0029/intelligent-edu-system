import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  courses = signal<any[]>([]);
  examsCount = signal<number>(0);
  schedulesCount = signal<number>(0);
  financeTotal = signal<number>(0);
  loading = signal<boolean>(true);

  constructor(
    public auth: AuthService, 
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.fetchAllStats();
  }

  fetchAllStats() {
    const courses$ = this.http.get<any[]>('http://localhost:8080/api/courses');
    const exams$ = this.http.get<any[]>('http://localhost:8080/api/exams');
    const schedules$ = this.http.get<any[]>('http://localhost:8080/api/schedules');
    const finance$ = this.http.get<any[]>('http://localhost:8080/api/finance');

    forkJoin({
      courses: courses$,
      exams: exams$,
      schedules: schedules$,
      finance: finance$
    }).subscribe({
      next: (results) => {
        this.courses.set(results.courses);
        this.examsCount.set(results.exams.length);
        this.schedulesCount.set(results.schedules.length);
        this.financeTotal.set(results.finance.reduce((acc, curr) => acc + (curr.amount || 0), 0));
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error fetching dashboard stats', err);
        this.loading.set(false);
      }
    });
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
