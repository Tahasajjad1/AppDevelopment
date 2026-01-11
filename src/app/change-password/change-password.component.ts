import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  template: `
    <div class="min-h-screen flex items-center justify-center bg-vertex-light relative overflow-hidden font-sans">
      
      <div class="absolute top-0 left-0 w-96 h-96 bg-vertex-blue rounded-full filter blur-[120px] opacity-10"></div>
      <div class="absolute bottom-0 right-0 w-96 h-96 bg-vertex-green rounded-full filter blur-[120px] opacity-10"></div>

      <div class="relative w-full max-w-md p-10 bg-white border border-slate-200 rounded-3xl shadow-xl z-10">
        
        <div class="text-center mb-10">
          <h2 class="text-3xl font-extrabold tracking-tight text-slate-900">
            SECURE <span class="text-vertex-blue">ACCOUNT</span>
          </h2>
          <div class="h-1 w-12 bg-vertex-green mx-auto mt-2 rounded-full"></div>
          <p class="text-slate-500 text-xs font-medium uppercase tracking-widest mt-4">
            Update Your Password
          </p>
        </div>

        <form (ngSubmit)="onSubmit()" #passwordForm="ngForm">
          
          <div class="mb-6 group">
            <label class="block text-slate-700 text-xs font-bold mb-2 uppercase tracking-wide group-focus-within:text-vertex-blue transition-colors">
              User ID
            </label>
            <input 
              type="text" 
              [(ngModel)]="data.vrxId" 
              name="vrxId"
              required 
              class="w-full px-4 py-3 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-vertex-blue focus:ring-2 focus:ring-vertex-blue/20 transition-all duration-300 placeholder-slate-400 invalid:border-red-300 invalid:text-red-600 focus:invalid:border-red-500 focus:invalid:ring-red-500/20"
              placeholder="e.g. VRX-ADMIN">
          </div>

          <div class="mb-6 group">
            <label class="block text-slate-700 text-xs font-bold mb-2 uppercase tracking-wide group-focus-within:text-vertex-blue transition-colors">
              New Password
            </label>
            <input 
              type="password" 
              [(ngModel)]="data.newPassword" 
              name="newPassword"
              required 
              minlength="8"
              class="w-full px-4 py-3 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-vertex-blue focus:ring-2 focus:ring-vertex-blue/20 transition-all duration-300 placeholder-slate-400 invalid:border-red-300 invalid:text-red-600 focus:invalid:border-red-500 focus:invalid:ring-red-500/20"
              placeholder="Min 8 chars">
          </div>

          <div class="mb-8 group">
            <label class="block text-slate-700 text-xs font-bold mb-2 uppercase tracking-wide group-focus-within:text-vertex-blue transition-colors">
              Confirm Password
            </label>
            <input 
              type="password" 
              [(ngModel)]="data.confirmPassword" 
              name="confirmPassword"
              required 
              class="w-full px-4 py-3 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-vertex-blue focus:ring-2 focus:ring-vertex-blue/20 transition-all duration-300 placeholder-slate-400 invalid:border-red-300 invalid:text-red-600 focus:invalid:border-red-500 focus:invalid:ring-red-500/20"
              placeholder="Retype password">
          </div>

          <button 
            type="submit" 
            [disabled]="passwordForm.invalid"
            class="w-full py-4 bg-gradient-to-r from-vertex-blue to-vertex-green hover:shadow-lg hover:shadow-vertex-blue/30 text-white font-bold rounded-xl transform transition active:scale-95 duration-200 flex justify-center items-center tracking-wider disabled:opacity-50 disabled:cursor-not-allowed">
            RESET PASSWORD
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
  styles: [`
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap');
    :host { font-family: 'Inter', sans-serif; }
  `]
})
export class ChangePasswordComponent {

  data = {
    vrxId: '',
    newPassword: '',
    confirmPassword: ''
  };

  notification = {
    show: false,
    type: 'error',
    title: '',
    message: ''
  };
  private toastTimeout: any;

  // IMPORTANT: Match this URL to your backend
  private apiUrl = 'https://literate-happiness-4wpv67q5vxx3jw6p-8080.app.github.dev/api/auth/change-password'; 

  constructor(private http: HttpClient, private router: Router) {}

  showToast(type: 'success' | 'error', title: string, message: string) {
    if (this.toastTimeout) {
      clearTimeout(this.toastTimeout);
    }
    this.notification = { show: true, type, title, message };
    
    this.toastTimeout = setTimeout(() => {
      this.closeNotification();
    }, 5000);
  }

  closeNotification() {
    this.notification.show = false;
  }

  onSubmit() {
    // 1. Validation: Passwords must match
    if (this.data.newPassword !== this.data.confirmPassword) {
        this.showToast('error', 'Validation Error', 'Passwords do not match!');
        return;
    }

    // 2. Send Request
    this.http.post(this.apiUrl, this.data, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          this.showToast('success', 'Success', 'Password updated! Redirecting...');
          
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 1500);
        },
        error: (err) => {
          console.error(err);

          if (err.status === 404) {
              this.showToast('error', 'Invalid User ID', 'The User ID you entered was not found.');
          } 
          else if (err.status === 400) {
              this.showToast('error', 'Validation Error', err.error);
          } 
          else {
              this.showToast('error', 'System Error', 'Unable to connect to the server.');
          }
        }
      });
  }
}