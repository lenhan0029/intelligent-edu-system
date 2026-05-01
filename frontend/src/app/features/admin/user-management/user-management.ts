import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

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
  rolesList = ['ROLE_STUDENT', 'ROLE_PARENT', 'ROLE_TEACHER', 'ROLE_CONTENT_CREATOR', 'ROLE_MODERATOR', 'ROLE_FINANCE_MANAGER', 'ROLE_ADMIN'];

  constructor(private http: HttpClient) {}

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
    // Only pass the roles as array of strings
    this.http.put(`http://localhost:8080/api/admin/users/${user.id}/roles`, { roles: user.newRoles }).subscribe({
      next: () => {
        alert('Roles updated successfully');
        this.fetchUsers();
      },
      error: (err) => {
        console.error('Error updating roles', err);
        alert('Failed to update roles');
      }
    });
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
