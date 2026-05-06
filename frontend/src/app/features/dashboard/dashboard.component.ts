import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userName = 'User';
  loading = signal<boolean>(true);

  constructor(
    public auth: AuthService, 
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    const user = this.auth.user();
    if (user) {
      this.userName = user.username || user.email?.split('@')[0] || 'Admin';
    }
    // Simulate loading or fetch actual data if needed
    setTimeout(() => this.loading.set(false), 500);
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
