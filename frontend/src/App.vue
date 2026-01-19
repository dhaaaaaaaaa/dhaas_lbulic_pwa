<template>
  <v-app>
    <Login v-if="!isLoggedIn" @login-success="login" />

    <template v-else>
      <v-app-bar color="primary" title="Venlab Dashboard">
        <v-spacer></v-spacer>

        <v-btn icon @click="toggleTheme" title="Design wechseln">
          <v-icon>{{ isDark ? 'mdi-weather-sunny' : 'mdi-weather-night' }}</v-icon>
        </v-btn>

        <v-btn icon @click="logout" title="Abmelden">
          <v-icon>mdi-logout</v-icon>
        </v-btn>
      </v-app-bar>

      <v-main>
        <v-container>
          <v-tabs v-model="tab" bg-color="primary" align-tabs="center">
            <v-tab value="analysis">Analysen</v-tab>
            <v-tab value="sample">Samples</v-tab>
            <v-tab value="box">Boxen</v-tab>
            <v-tab value="boxpos">BoxPos</v-tab>
            <v-tab value="view">Gesamtübersicht</v-tab>
            <v-tab value="logs">Logs</v-tab>
          </v-tabs>

          <v-window v-model="tab">
            <v-window-item value="analysis"><AnalysisTable /></v-window-item>
            <v-window-item value="sample"><SampleTable /></v-window-item>
            <v-window-item value="box"><BoxTable /></v-window-item>
            <v-window-item value="boxpos"><BoxPosTable /></v-window-item>
            <v-window-item value="view"><SampleBoxPosTable /></v-window-item>
            <v-window-item value="logs"><LogTable /></v-window-item>
          </v-window>
        </v-container>
      </v-main>
    </template>
  </v-app>
</template>

<script>
// 1. Der wichtigste Fix: Axios importieren
import axios from 'axios';

// Import der Komponenten
import Login from './components/Login.vue';
import AnalysisTable from './components/AnalysisTable.vue';
import SampleTable from './components/SampleTable.vue';
import BoxTable from './components/BoxTable.vue';
import BoxPosTable from './components/BoxPosTable.vue';
import SampleBoxPosTable from './components/SampleBoxPosTable.vue';
import LogTable from './components/LogTable.vue';

export default {
  components: {
    Login,
    AnalysisTable,
    SampleTable,
    BoxTable,
    BoxPosTable,
    SampleBoxPosTable,
    LogTable
  },
  data: () => ({
    tab: null,
    isLoggedIn: false, // Standard: Ausgeloggt
  }),
  computed: {
    isDark() {
      return this.$vuetify.theme.global.name === 'dark';
    }
  },
  mounted() {
    // Theme-Logik
    const savedTheme = localStorage.getItem('venlab-theme');
    if (savedTheme) this.$vuetify.theme.global.name = savedTheme;

    // Axios Interceptor für globales Error Handling
    axios.interceptors.response.use(
        response => response,
        error => {
          if (error.response && error.response.status === 401) {
            this.logout();
          }
          return Promise.reject(error);
        }
    );

    // Login-Status beim Neuladen wiederherstellen
    const savedKey = localStorage.getItem('venlab-api-key');
    if (savedKey) {
      axios.defaults.headers.common['X-API-KEY'] = savedKey;
      this.isLoggedIn = true;
    }
  },
  methods: {
    toggleTheme() {
      const newTheme = this.$vuetify.theme.global.name === 'dark' ? 'light' : 'dark';
      this.$vuetify.theme.global.name = newTheme;
      localStorage.setItem('venlab-theme', newTheme);
    },
    login() {
      this.isLoggedIn = true;
      // Hinweis: Du speicherst oben 'venlab-api-key', stelle sicher,
      // dass dieser beim Login-Prozess auch gesetzt wird!
    },
    logout() {
      this.isLoggedIn = false;
      localStorage.removeItem('venlab-api-key'); // Key löschen
      localStorage.removeItem('venlab-auth');
      delete axios.defaults.headers.common['X-API-KEY']; // Header säubern
      this.tab = null;
    }
  }
}
</script>