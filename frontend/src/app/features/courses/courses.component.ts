import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-courses',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})
export class CoursesComponent implements OnInit {
  courses = [
    {
      id: '1',
      name: 'Advanced Web Development with Angular',
      description: 'Master modern web applications with Angular 17, RxJS, and State Management. Learn how to build scalable and performant apps.',
      teacherName: 'Alex Johnson',
      price: 199,
      category: 'Development',
      thumbnail: 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?q=80&w=800&auto=format&fit=crop'
    },
    {
      id: '2',
      name: 'Data Science & Machine Learning',
      description: 'From Python basics to advanced neural networks. This comprehensive course covers all the essential tools for a data scientist.',
      teacherName: 'Sarah Smith',
      price: 249,
      category: 'Data Science',
      thumbnail: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?q=80&w=800&auto=format&fit=crop'
    },
    {
      id: '3',
      name: 'UI/UX Design Masterclass',
      description: 'Learn the principles of design, user psychology, and how to use Figma like a pro. Create stunning interfaces that users love.',
      teacherName: 'David Chen',
      price: 149,
      category: 'Design',
      thumbnail: 'https://images.unsplash.com/photo-1586717791821-3f44a563eb4c?q=80&w=800&auto=format&fit=crop'
    },
    {
      id: '4',
      name: 'Business Strategy & Leadership',
      description: 'Develop the skills needed to lead teams and grow businesses in the modern economy. Focus on practical leadership and strategy.',
      teacherName: 'Emma Watson',
      price: 299,
      category: 'Business',
      thumbnail: 'https://images.unsplash.com/photo-1507679799987-c73779587ccf?q=80&w=800&auto=format&fit=crop'
    }
  ];

  constructor() {}

  ngOnInit(): void {}
}
