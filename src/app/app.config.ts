import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import {
  provideRouter,
  withComponentInputBinding,
  withViewTransitions,
} from '@angular/router';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), // High performance change detection
    provideRouter(routes, withViewTransitions()), // Enables smooth "fading" between pages
    provideHttpClient(withFetch()), // Modern HTTP client using Fetch API
    provideAnimationsAsync(), // Required for your UI animations
  ],
};
