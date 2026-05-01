import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  
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
}
