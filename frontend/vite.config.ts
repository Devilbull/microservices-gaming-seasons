import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 7152,   // ← ovde staviš koji hoćeš
    host: true    // da bi bio dostupan i na mreži (192.168.x.x)
  }
})