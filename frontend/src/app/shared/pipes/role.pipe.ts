import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formatRole',
  standalone: true
})
export class RolePipe implements PipeTransform {
  transform(value: string | undefined): string {
    if (!value) return '';
    
    const roleMap: { [key: string]: string } = {
      'ROLE_SUPERADMIN': 'Super Admin',
      'ROLE_ADMIN': 'Administrator',
      'ROLE_TEACHER': 'Teacher',
      'ROLE_STUDENT': 'Student',
      'ROLE_PARENT': 'Parent',
      'ROLE_FINANCE_MANAGER': 'Finance',
      'ROLE_CONTENT_CREATOR': 'Content Creator',
      'ROLE_REVIEWER': 'Reviewer'
    };
    
    return roleMap[value] || value.replace('ROLE_', '').toLowerCase();
  }
}
