import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

import { RolePipe } from '../../shared/pipes/role.pipe';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, RolePipe],
  templateUrl: './main-layout.html',
  styleUrls: ['./main-layout.css']
})
export class MainLayout implements OnInit {
  isCollapsed = false;
  userName = 'User';
  userRole = '';

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit() {
    const user = this.authService.user();
    if (user) {
      this.userName = user.username || user.email?.split('@')[0] || 'Student';
      this.userRole = user.role || '';
    }
  }

  toggleSidebar() {
    this.isCollapsed = !this.isCollapsed;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
