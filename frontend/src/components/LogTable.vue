<template>
  <v-container>
    <v-card class="elevation-2">
      <v-card-title class="text-primary">System Logs</v-card-title>

      <v-row class="px-4 pb-2">
        <v-col cols="12">
          <v-select
              v-model="selectedHeaders"
              :items="allHeaders"
              label="Spalten anzeigen/ausblenden"
              multiple
              return-object
              density="compact"
              variant="outlined"
              hide-details
              chips
              closable-chips
          ></v-select>
        </v-col>
      </v-row>

      <v-data-table-server
          v-model:items-per-page="itemsPerPage"
          :headers="selectedHeaders"
          :items="serverItems"
          :items-length="totalItems"
          :loading="loading"
          item-value="logId"
          @update:options="loadItems"
      >
        <template v-slot:item.level="{ item }">
          <v-chip :color="getLevelColor(item.level)" size="small" label>
            {{ item.level }}
          </v-chip>
        </template>

        <template v-slot:item.dateCreated="{ item }">
          {{ new Date(item.dateCreated).toLocaleString() }}
        </template>
      </v-data-table-server>
    </v-card>
  </v-container>
</template>

<script>
import LogService from '../services/LogService';

export default {
  data: () => ({
    loading: false,
    itemsPerPage: 10,
    totalItems: 0,
    serverItems: [],

    allHeaders: [
      { title: 'ID', key: 'logId' },
      { title: 'Zeitpunkt', key: 'dateCreated' },
      { title: 'Level', key: 'level' },
      { title: 'Nachricht', key: 'info', sortable: false },
    ],
    selectedHeaders: [],
  }),

  created() {
    this.selectedHeaders = this.allHeaders;
  },

  methods: {
    getLevelColor(level) {
      if (!level) return 'grey';
      const l = level.toUpperCase();
      if (l === 'ERROR') return 'red';
      if (l === 'WARN') return 'orange';
      if (l === 'INFO') return 'blue';
      return 'grey';
    },
    loadItems({ page, itemsPerPage, sortBy = [] }) {
      this.loading = true;
      const sortKey = sortBy.length ? sortBy[0].key : 'logId';
      const sortOrder = sortBy.length ? (sortBy[0].order === 'desc' ? 'desc' : 'asc') : 'desc';

      LogService.getAll(page - 1, itemsPerPage, sortKey, sortOrder)
          .then(res => {
            this.serverItems = res.data.content;
            this.totalItems = res.data.totalElements;
            this.loading = false;
          })
          .catch(() => { this.loading = false; });
    }
  }
}
</script>