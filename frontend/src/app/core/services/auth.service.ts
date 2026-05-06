import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

const ROLE_WEIGHTS: Record<string, number> = {
  'ROLE_SUPERADMIN': 100,
  'ROLE_ADMIN': 80,
  'ROLE_TEACHER': 60,
  'ROLE_REVIEWER': 60,
  'ROLE_CONTENT_CREATOR': 50,
  'ROLE_FINANCE': 50,
  'ROLE_STUDENT': 20,
  'ROLE_PARENT': 20,
  'ROLE_USER': 10
};

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/api/auth`;
  
  user = signal<any>(null);
  token = signal<string | null>(localStorage.getItem('token'));

  constructor(private http: HttpClient) {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      this.user.set(JSON.parse(savedUser));
    }
  }

  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((res: any) => {
        this.token.set(res.accessToken);
        this.user.set(res);
        localStorage.setItem('token', res.accessToken);
        localStorage.setItem('user', JSON.stringify(res));
      })
    );
  }

  getAccessToken(): string | null {
    return this.token();
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  refreshToken(): Observable<any> {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http.post(`${this.apiUrl}/refresh-token`, { refreshToken }).pipe(
      tap((res: any) => {
        this.token.set(res.accessToken);
        localStorage.setItem('token', res.accessToken);
      })
    );
  }

  logout() {
    this.token.set(null);
    this.user.set(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('refreshToken');
  }

  isAuthenticated(): boolean {
    return !!this.token();
  }

  getRoles(): string[] {
    const currentUser = this.user();
    return currentUser && currentUser.roles ? currentUser.roles : [];
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }

  getMaxRoleWeight(): number {
    const roles = this.getRoles();
    return Math.max(...roles.map(r => ROLE_WEIGHTS[r] || 0), 0);
  }

  isAtLeast(role: string): boolean {
    return this.getMaxRoleWeight() >= (ROLE_WEIGHTS[role] || 0);
  }

  canManage(targetUserRoles: any[]): boolean {
    const myMaxWeight = this.getMaxRoleWeight();
    const targetRoles = targetUserRoles.map(r => typeof r === 'string' ? r : r.name);
    const targetMaxWeight = Math.max(...targetRoles.map(r => ROLE_WEIGHTS[r] || 0), 0);
    
    if (this.hasRole('ROLE_SUPERADMIN')) return true;
    if (this.hasRole('ROLE_ADMIN')) {
      return targetMaxWeight < 80; // Admin can manage everyone EXCEPT other Admins and Superadmins
    }
    return false;
  }
}
