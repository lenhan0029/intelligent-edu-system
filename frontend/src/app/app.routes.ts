import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { Register } from './features/auth/register/register';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { MainLayout } from './layout/main-layout/main-layout';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: Register },
  { 
    path: '', 
    component: MainLayout,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: '/login' }
];
