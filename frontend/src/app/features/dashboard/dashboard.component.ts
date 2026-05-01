import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { CourseService } from '../../core/services/course.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  courses = signal<any[]>([]);
  loading = signal<boolean>(true);

  constructor(
    public auth: AuthService, 
    private courseService: CourseService,
    private router: Router
  ) {}

  ngOnInit() {
    this.fetchCourses();
  }

  fetchCourses() {
    this.courseService.getCourses().subscribe({
      next: (data) => {
        this.courses.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error fetching courses', err);
        this.loading.set(false);
      }
    });
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
