import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // Local dev: forward /api to the Spring Boot app, so the frontend uses the
    // same relative '/api' path as the deployed (Nginx) setup.
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
