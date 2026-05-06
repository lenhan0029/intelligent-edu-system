import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-exam-session',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './exam-session.component.html',
  styleUrls: ['./exam-session.component.css']
})
export class ExamSessionComponent implements OnInit {
  selectedOption: string = '';

  constructor() {}

  ngOnInit(): void {}
}
