import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RolePipe } from '../../../shared/pipes/role.pipe';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RolePipe],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  showModal = false;
  searchTerm = '';
  selectedRole = '';
  selectedStatus = '';

  users = [
    { name: 'Admin User', email: 'admin@edu.com', role: 'ROLE_ADMIN', status: 'ACTIVE', organizationName: 'Main Campus' },
    { name: 'John Teacher', email: 'teacher@edu.com', role: 'ROLE_TEACHER', status: 'ACTIVE', organizationName: 'CS Dept' },
    { name: 'Jane Student', email: 'jane@student.com', role: 'ROLE_STUDENT', status: 'ACTIVE', organizationName: 'Main Campus' },
    { name: 'Locked User', email: 'locked@edu.com', role: 'ROLE_STUDENT', status: 'LOCKED', organizationName: 'Physics Dept' }
  ];

  filteredUsers = [...this.users];

  constructor() {}

  ngOnInit(): void {}

  filterUsers() {
    this.filteredUsers = this.users.filter(u => {
      const matchSearch = u.name.toLowerCase().includes(this.searchTerm.toLowerCase()) || 
                          u.email.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchRole = !this.selectedRole || u.role === this.selectedRole;
      const matchStatus = !this.selectedStatus || u.status === this.selectedStatus;
      return matchSearch && matchRole && matchStatus;
    });
  }

  openAddUserModal() {
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  saveUser() {
    this.closeModal();
  }
}
