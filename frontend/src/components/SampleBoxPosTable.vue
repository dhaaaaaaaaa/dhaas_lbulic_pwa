<template>
  <v-container>
    <v-card class="elevation-2">
      <v-card-title class="text-primary">
        Gesamtübersicht
      </v-card-title>
      <v-card-subtitle>
        Verknüpfte Daten: Sample + Box (Read Only)
      </v-card-subtitle>

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
          item-value="sId"
          @update:options="loadItems"
      >
        <template v-slot:item.sStamp="{ item }">
          {{ formatDateTime(item.sStamp) }}
        </template>

        <template v-slot:item.sampleInfo="{ item }">
          <div style="font-weight: bold">{{ item.sName || '---' }}</div>
          <div style="font-size: 0.8em; color: grey">ID: {{ item.sId }}</div>
        </template>

        <template v-slot:item.boxInfo="{ item }">
          <div style="font-weight: bold">{{ item.bName || '---' }}</div>
          <div style="font-size: 0.8em; color: grey">BoxID: {{ item.bId }} | Pos: {{ item.bposId }}</div>
        </template>

      </v-data-table-server>
    </v-card>
  </v-container>
</template>

<script>
import Service from '../services/SampleBoxPosService';

export default {
  name: "SampleBoxPosTable",
  data: () => ({
    loading: false,
    itemsPerPage: 10,
    totalItems: 0,
    serverItems: [],

    allHeaders: [
      {title: 'Zeitstempel', key: 'sStamp', width: '180px'},
      {title: 'Sample Info', key: 'sampleInfo', sortable: false},
      {title: 'Lagerort (Box)', key: 'boxInfo', sortable: false},
    ],
    selectedHeaders: [],
  }),

  created() {
    this.selectedHeaders = this.allHeaders;
  },

  methods: {
    formatDateTime(val) {
      if (!val) return '-';
      return val.toString().replace('T', ' ').substring(0, 19);
    },

    loadItems({page, itemsPerPage, sortBy = []}) {
      this.loading = true;
      let sortKey = 'sId';
      if (sortBy.length) sortKey = sortBy[0].key;
      if (sortKey === 'sampleInfo') sortKey = 'sName';
      if (sortKey === 'boxInfo') sortKey = 'bName';

      const sortOrder = sortBy.length ? (sortBy[0].order === 'desc' ? 'desc' : 'asc') : 'asc';

      Service.getAll(page - 1, itemsPerPage, sortKey, sortOrder)
          .then(res => {
            this.serverItems = res.data.content;
            this.totalItems = res.data.totalElements;
            this.loading = false;
          })
          .catch(err => {
            console.error(err);
            this.loading = false;
          });
    }
  },
}
</script>