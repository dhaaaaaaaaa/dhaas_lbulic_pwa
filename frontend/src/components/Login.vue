<template>
  <v-container class="fill-height justify-center">
    <v-card width="400" class="elevation-12">
      <v-toolbar color="primary" title="VenLab Login"></v-toolbar>
      <v-card-text class="pa-4">
        <v-form @submit.prevent="handleLogin">
          <v-text-field
              v-model="username"
              label="Benutzer (beliebig)"
              prepend-inner-icon="mdi-account"
              variant="outlined"
          ></v-text-field>
          <v-text-field
              v-model="password"
              label="Passwort"
              type="password"
              prepend-inner-icon="mdi-lock"
              variant="outlined"
              class="mt-2"
          ></v-text-field>

          <v-alert v-if="error" type="error" density="compact" class="mt-4" variant="tonal">
            Passwort falsch!
          </v-alert>
        </v-form>
      </v-card-text>
      <v-card-actions class="pa-4">
        <v-btn block color="primary" size="large" variant="elevated" @click="handleLogin" :loading="loading">
          Anmelden
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data: () => ({
    username: 'admin',
    password: '',
    error: false,
    loading: false
  }),
  methods: {
    handleLogin() {
      this.loading = true;
      this.error = false;

      // 1. Passwort prüfen (Backend fragen)
      axios.post('/api/auth/login', {
        username: this.username,
        password: this.password
      })
          .then(() => {
            // 2. WENN OK: Passwort als API-Key speichern
            localStorage.setItem('venlab-api-key', this.password);
            localStorage.setItem('venlab-auth', 'true');

            // 3. Axios konfigurieren: Ab jetzt immer den Key mitschicken!
            axios.defaults.headers.common['X-API-KEY'] = this.password;

            // 4. Der App Bescheid sagen
            this.$emit('login-success');
          })
          .catch(err => {
            console.error("Login Fehler:", err);
            this.error = true;
            this.password = '';
          })
          .finally(() => {
            this.loading = false;
          });
    }
  }
}
</script>