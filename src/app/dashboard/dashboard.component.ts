import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="min-h-screen bg-gray-900 text-white p-10">
      <h1 class="text-4xl font-bold text-vertex-primary">Welcome to Dashboard</h1>
      <p class="mt-4 text-gray-400">System Status: Online</p>
    </div>
  `,
})
export class DashboardComponent {}
