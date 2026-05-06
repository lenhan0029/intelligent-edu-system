import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-organization-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">Organizations</h2>
        <button class="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition">
          + Add Organization
        </button>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        @for (org of organizations(); track org.id) {
          <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-5 hover:shadow-md transition">
            <div class="flex items-center space-x-4 mb-4">
              <div class="w-12 h-12 bg-indigo-100 rounded-lg flex items-center justify-center text-2xl">
                🏢
              </div>
              <div>
                <h3 class="font-semibold text-lg text-gray-900">{{ org.name }}</h3>
                <span class="text-sm text-gray-500">{{ org.type }}</span>
              </div>
            </div>
            <p class="text-gray-600 text-sm mb-4">{{ org.address }}</p>
            <div class="flex justify-between items-center text-sm">
              <span class="text-indigo-600 font-medium">Admin: {{ org.adminEmail }}</span>
              <div class="flex space-x-2">
                <button class="text-gray-400 hover:text-indigo-600">✏️</button>
                <button class="text-gray-400 hover:text-red-600">🗑️</button>
              </div>
            </div>
          </div>
        }
      </div>
    </div>
  `
})
export class OrganizationListComponent implements OnInit {
  organizations = signal<any[]>([]);
  private apiUrl = 'http://localhost:8080/api/org';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<any[]>(this.apiUrl).subscribe(res => {
      this.organizations.set(res);
    });
  }
}
