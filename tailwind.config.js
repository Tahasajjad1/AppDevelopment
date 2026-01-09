/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        'vertex-blue': '#2563eb', // Professional Royal Blue
        'vertex-green': '#10b981', // Success/Mint Green
        'vertex-light': '#f8fafc', // Soft Slate Background
      },
    },
  },
  plugins: [],
};
