import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  template: `
    <div class="min-h-screen flex items-center justify-center bg-vertex-light relative overflow-hidden font-sans">
  
      <div class="absolute top-0 left-0 w-96 h-96 bg-vertex-blue rounded-full filter blur-[120px] opacity-10"></div>
      <div class="absolute bottom-0 right-0 w-96 h-96 bg-vertex-green rounded-full filter blur-[120px] opacity-10"></div>

      <div class="relative w-full max-w-md p-10 bg-white border border-slate-200 rounded-3xl shadow-xl z-10">
        
        <div class="text-center mb-10">
          <h1 class="text-3xl font-extrabold tracking-tight text-slate-900">
            TRADE <span class="text-vertex-blue">PORTAL</span>
          </h1>
          <div class="h-1 w-12 bg-vertex-green mx-auto mt-2 rounded-full"></div>
          <p class="text-slate-500 text-xs font-medium uppercase tracking-widest mt-4">Enterprise Access Portal</p>
        </div>

        <form (ngSubmit)="onLogin()">
          <div class="mb-6 group">
            <label class="block text-slate-700 text-xs font-bold mb-2 uppercase tracking-wide group-focus-within:text-vertex-blue transition-colors">User ID</label>
            <input 
              type="text" 
              [(ngModel)]="credentials.vrxId" 
              name="vrxId"
              required 
              class="w-full px-4 py-3 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-vertex-blue focus:ring-2 focus:ring-vertex-blue/20 transition-all duration-300 placeholder-slate-400 invalid:border-red-300 invalid:text-red-600 focus:invalid:border-red-500 focus:invalid:ring-red-500/20"
              placeholder="User ID">
          </div>

          <div class="mb-8 group">
            <label class="block text-slate-700 text-xs font-bold mb-2 uppercase tracking-wide group-focus-within:text-vertex-blue transition-colors">Password</label>
            <input 
              type="password" 
              [(ngModel)]="credentials.password" 
              name="password"
              required
              class="w-full px-4 py-3 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-vertex-blue focus:ring-2 focus:ring-vertex-blue/20 transition-all duration-300 placeholder-slate-400 invalid:border-red-300 invalid:text-red-600 focus:invalid:border-red-500 focus:invalid:ring-red-500/20"
              placeholder="••••••••">
          </div>

          <button 
            type="submit" 
            [disabled]="!credentials.vrxId || !credentials.password"
            class="w-full py-4 bg-gradient-to-r from-vertex-blue to-vertex-green hover:shadow-lg hover:shadow-vertex-blue/30 text-white font-bold rounded-xl transform transition active:scale-95 duration-200 flex justify-center items-center tracking-wider disabled:opacity-50 disabled:cursor-not-allowed">
            <span *ngIf="!isLoading">LOGIN</span>
            <span *ngIf="isLoading" class="animate-spin h-5 w-5 border-2 border-white border-t-transparent rounded-full"></span>
          </button>
        </form>

        <div class="mt-8 text-center pt-4">
          <p class="text-[10px] text-slate-400 font-semibold tracking-tighter">SECURED BY VERTEX GUARD • SYSTEM VERSION 1.0.0</p>
        </div>
      </div>

      <div *ngIf="notification.show" 
           class="fixed top-5 right-5 z-50 transform transition-all duration-500 ease-out"
           [ngClass]="{
             'translate-x-0 opacity-100': notification.show,
             'translate-x-10 opacity-0': !notification.show
           }">
        <div class="flex items-start p-4 rounded-lg shadow-2xl border-l-4 min-w-[320px] backdrop-blur-sm bg-white"
             [ngClass]="{
               'border-red-500': notification.type === 'error',
               'border-green-500': notification.type === 'success'
             }">
          
          <div class="mr-3 mt-0.5">
            <span *ngIf="notification.type === 'error'" class="text-xl">⛔</span>
            <span *ngIf="notification.type === 'success'" class="text-xl">✅</span>
          </div>

          <div class="flex-1">
            <h4 class="font-bold text-sm uppercase tracking-wider" 
                [ngClass]="{'text-red-600': notification.type === 'error', 'text-green-600': notification.type === 'success'}">
              {{ notification.title }}
            </h4>
            <p class="text-xs font-medium text-slate-500 mt-1 leading-relaxed">{{ notification.message }}</p>
          </div>

          <button (click)="closeNotification()" 
                  class="ml-3 text-slate-400 hover:text-slate-600 transition-colors focus:outline-none">
            <span class="text-xl font-bold leading-none">&times;</span>
          </button>

        </div>
      </div>

    </div>
  `,
  styles: [
    `
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap');
    :host { font-family: 'Inter', sans-serif; }
  `,
  ],
})
export class LoginComponent {
  credentials = { vrxId: '', password: '' };
  isLoading = false;
  private toastTimeout: any;

  notification = {
    show: false,
    type: 'error',
    title: '',
    message: '',
  };

  constructor(private http: HttpClient, private router: Router) {}

  // --- NEW: LOGGING SYSTEM ---
  // Sends logs to Spring Boot (which writes to angular.log)
  logToBackend(level: string, message: string) {
    // 1. Keep local console for dev convenience
    console.log(`[${level}] ${message}`);

    // 2. Send to Backend
    this.http
      .post('http://localhost:8080/api/logs/client', {
        level: level,
        message: message,
        timestamp: new Date().toISOString(),
      })
      .subscribe({
        error: (e) => console.error('Failed to send log to server', e),
      });
  }

  showToast(type: 'success' | 'error', title: string, message: string) {
    if (this.toastTimeout) {
      clearTimeout(this.toastTimeout);
    }
    this.notification = { show: true, type, title, message };
    this.toastTimeout = setTimeout(() => {
      this.closeNotification();
    }, 30000);
  }

  closeNotification() {
    this.notification.show = false;
  }

  onLogin() {
    // 1. Log the attempt
    this.logToBackend(
      'INFO',
      `Login attempt started for User: ${this.credentials.vrxId}`
    );

    this.isLoading = true;
    const loginUrl = 'http://localhost:8080/api/auth/login';

    this.http.post(loginUrl, this.credentials).subscribe({
      next: (response: any) => {
        // 2. Log Success
        this.logToBackend(
          'INFO',
          `Login SUCCESS for User: ${this.credentials.vrxId}`
        );

        this.showToast(
          'success',
          'Access Granted',
          'Redirecting to Dashboard...'
        );

        setTimeout(() => {
          this.isLoading = false;
          localStorage.setItem('currentUser', JSON.stringify(response));
          this.router.navigate(['/dashboard']);
        }, 1500);
      },
      error: (error) => {
        // 3. Log Failure
        this.logToBackend(
          'ERROR',
          `Login FAILED for User: ${this.credentials.vrxId}. Reason: ${error.status} - ${error.message}`
        );

        this.isLoading = false;

        if (error.status === 0) {
          this.showToast(
            'error',
            'Connection Error',
            'Unable to reach the Server.'
          );
        } else if (error.status === 401) {
          this.showToast(
            'error',
            'Access Denied',
            'Invalid User ID or Password.'
          );
        } else {
          const msg =
            error.error?.message ||
            'Authentication Failed due to an unknown error.';
          this.showToast('error', 'System Error', msg);
        }
      },
    });
  }
}
