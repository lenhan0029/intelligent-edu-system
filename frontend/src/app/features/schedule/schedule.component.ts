import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-schedule',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit {
  schedules: any[] = [];
  loading = true;
  days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchSchedule();
  }

  fetchSchedule() {
    this.http.get<any[]>('http://localhost:8080/api/schedules').subscribe({
      next: (data) => {
        this.schedules = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching schedule', err);
        this.loading = false;
      }
    });
  }

  getSchedulesForDay(day: string) {
    return this.schedules.filter(s => s.dayOfWeek === day);
  }
}
