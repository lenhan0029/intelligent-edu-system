import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { Register } from './features/auth/register/register';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { MainLayout } from './layout/main-layout/main-layout';
import { RoleGuard } from './core/guards/role.guard';
import { UserListComponent } from './features/users/user-list/user-list.component';
import { CoursesComponent } from './features/courses/courses.component';
import { ExamsComponent } from './features/exams/exams.component';
import { ScheduleComponent } from './features/schedule/schedule.component';
import { FinanceComponent } from './features/finance/finance.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: Register },
  { 
    path: '', 
    component: MainLayout,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'users', component: UserListComponent, canActivate: [RoleGuard], data: { roles: ['ROLE_ADMIN', 'ROLE_SUPERADMIN'] } },
      { path: 'courses', component: CoursesComponent },
      { path: 'courses/:id', loadComponent: () => import('./features/courses/course-detail/course-detail.component').then(m => m.CourseDetailComponent) },
      { path: 'exams', component: ExamsComponent },
      { path: 'schedule', component: ScheduleComponent },
      { path: 'finance', component: FinanceComponent },
      { path: 'chat', loadComponent: () => import('./features/chat/chat.component').then(m => m.ChatComponent) },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: '/login' }
];
