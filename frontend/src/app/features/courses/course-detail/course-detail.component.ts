import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';

@Component({
  selector: 'app-course-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './course-detail.component.html',
  styleUrls: ['./course-detail.component.css']
})
export class CourseDetailComponent implements OnInit {
  courseId: string | null = null;
  activeTab: string = 'overview';
  
  course: any = {
    id: '1',
    name: 'Advanced Web Development with Angular',
    description: 'Master modern web applications with Angular 17, RxJS, and State Management. Learn how to build scalable and performant apps from scratch. This course covers everything from basic architecture to advanced deployment strategies.',
    teacherName: 'Alex Johnson',
    price: 199,
    category: 'Development',
    thumbnail: 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?q=80&w=800&auto=format&fit=crop'
  };

  lessons = [
    { title: 'Introduction to Angular 17', duration: '12:45', type: 'Video' },
    { title: 'Understanding Components & Modules', duration: '25:30', type: 'Video' },
    { title: 'Reactive Programming with RxJS', duration: '45:00', type: 'Video' },
    { title: 'State Management with Signals', duration: '30:15', type: 'Video' },
    { title: 'Building a Real-world Project', duration: '1:15:00', type: 'Video' }
  ];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.courseId = this.route.snapshot.paramMap.get('id');
    // In a real app, you would fetch the course by ID from a service
  }
}
