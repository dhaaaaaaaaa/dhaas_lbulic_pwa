plugins: [
  vue()
],
  resolve: {
  alias: {
    '@': fileURLToPath(new URL('./src', import.meta.url))
  }
},
server: {
  host: '0.0.0.0',
    port: 3000,
      hmr: {
    host: 'localhost',
    },
  watch: {
    usePolling: true
  },
  proxy: {
    '/api': {
      target: 'http://localhost:9090',
        changeOrigin: true,
          secure: false
    }
  }
},
preview: {
  port: 4173,
    proxy: {
    '/api': {
      target: 'http://localhost:9090',
        changeOrigin: true,
          secure: false
    }
  }
}
})