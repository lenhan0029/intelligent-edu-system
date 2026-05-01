import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './main-layout.html',
  styleUrls: ['./main-layout.css']
})
export class MainLayout implements OnInit {
  isSidebarCollapsed = false;
  userName = 'User';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    const user = this.authService.user();
    if (user) {
      this.userName = user.username || user.email?.split('@')[0] || 'Student';
    }
  }

  toggleSidebar() {
    this.isSidebarCollapsed = !this.isSidebarCollapsed;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
