import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-management.html',
  styleUrls: ['./user-management.css']
})
export class UserManagement implements OnInit {
  users: any[] = [];
  loading = true;
  rolesList = ['ROLE_STUDENT', 'ROLE_PARENT', 'ROLE_TEACHER', 'ROLE_CONTENT_CREATOR', 'ROLE_MODERATOR', 'ROLE_FINANCE_MANAGER', 'ROLE_ADMIN', 'ROLE_SUPERADMIN'];

  constructor(private http: HttpClient, public authService: AuthService) {}

  ngOnInit() {
    this.fetchUsers();
  }

  fetchUsers() {
    this.http.get<any[]>('http://localhost:8080/api/admin/users').subscribe({
      next: (data) => {
        this.users = data.map(u => ({...u, newRoles: u.roles.map((r: any) => r.name)}));
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching users', err);
        this.loading = false;
      }
    });
  }

  updateRoles(user: any) {
    this.http.put(`http://localhost:8080/api/admin/users/${user.id}/roles`, { roles: user.newRoles }).subscribe({
      next: () => {
        alert('Roles updated successfully');
        this.fetchUsers();
      },
      error: (err: any) => {
        alert(err.error?.message || 'Failed to update roles');
      }
    });
  }

  toggleUserStatus(user: any) {
    const newStatus = !user.active;
    this.http.put(`http://localhost:8080/api/admin/users/${user.id}/status`, { isActive: newStatus }).subscribe({
      next: () => {
        user.active = newStatus;
        alert(`User ${newStatus ? 'unlocked' : 'locked'} successfully`);
      },
      error: (err: any) => {
        alert(err.error?.message || 'Failed to update status');
      }
    });
  }

  deleteUser(user: any) {
    if (confirm(`Are you sure you want to delete user ${user.username}?`)) {
      this.http.delete(`http://localhost:8080/api/admin/users/${user.id}`).subscribe({
        next: () => {
          alert('User deleted successfully');
          this.fetchUsers();
        },
        error: (err: any) => {
          alert(err.error?.message || 'Failed to delete user');
        }
      });
    }
  }

  toggleRole(user: any, role: string, event: Event) {
    const isChecked = (event.target as HTMLInputElement).checked;
    if (isChecked) {
      if (!user.newRoles.includes(role)) {
        user.newRoles.push(role);
      }
    } else {
      user.newRoles = user.newRoles.filter((r: string) => r !== role);
    }
  }
}
