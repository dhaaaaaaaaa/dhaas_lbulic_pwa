<template>
  <v-container>
    <v-data-table-server
        v-model:items-per-page="itemsPerPage"
        :headers="selectedHeaders"
        :items="serverItems"
        :items-length="totalItems"
        :loading="loading"
        item-value="id"
        @update:options="loadItems"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Analysen</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="openDialog()">Neue Analyse</v-btn>
        </v-toolbar>

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
      </template>

      <template v-slot:item.sStamp="{ item }">
        {{ formatDateTime(item.sStamp) }}
      </template>

      <template v-slot:item.boxId="{ item }">
        {{ item.boxId || '-' }}
      </template>

      <template v-slot:item.actions="{ item }">
        <v-icon size="small" class="me-2" @click="editItem(item)">mdi-pencil</v-icon>
        <v-icon size="small" color="red" @click="deleteItem(item)">mdi-delete</v-icon>
      </template>
    </v-data-table-server>

    <v-dialog v-model="dialog" max-width="600px">
      <v-card>
        <v-card-title>{{ formTitle }}</v-card-title>
        <v-card-text>
          <v-container>
            <v-form ref="form" v-model="valid" @submit.prevent>
              <v-row>
                <v-col cols="6">
                  <v-text-field
                      v-model="editedItem.sId"
                      label="Sample ID (s_id)"
                      :disabled="editedIndex > -1"
                      :rules="[rules.required]">
                  </v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      v-model="editedItem.sStamp"
                      label="Zeit (s_stamp)"
                      :disabled="editedIndex > -1">
                  </v-text-field>
                </v-col>

                <v-col cols="12" v-if="editedIndex > -1">
                  <span class="text-caption">Daten</span>
                  <v-row dense>
                    <v-col cols="6"><v-text-field v-model="editedItem.sampleName" label="Name" density="compact" ></v-text-field></v-col>
                    <v-col cols="6"><v-text-field v-model="editedItem.boxId" label="Box" density="compact" ></v-text-field></v-col>
                    <v-col cols="4"><v-text-field v-model="editedItem.weightNet" label="Netto" density="compact" ></v-text-field></v-col>
                    <v-col cols="4"><v-text-field v-model="editedItem.weightBru" label="Brutto" density="compact" ></v-text-field></v-col>
                  </v-row>
                </v-col>

                <v-col cols="6"><v-text-field v-model="editedItem.pol" label="Pol"></v-text-field></v-col>
                <v-col cols="6"><v-text-field v-model="editedItem.nat" label="Nat"></v-text-field></v-col>
                <v-col cols="12"><v-text-field v-model="editedItem.comment" label="Kommentar"></v-text-field></v-col>
              </v-row>
            </v-form>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-btn color="grey" variant="text" @click="close">Abbrechen</v-btn>
          <v-btn color="blue" variant="text" @click="save">Speichern</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-snackbar v-model="snackbar" :color="snackbarColor" timeout="5000">
      {{ snackbarText }}
      <template v-slot:actions><v-btn variant="text" @click="snackbar = false">X</v-btn></template>
    </v-snackbar>
  </v-container>
</template>

<script>
import AnalysisService from '../services/AnalysisService';

export default {
  data: () => ({
    valid: false,
    snackbar: false, snackbarText: '', snackbarColor: 'success',
    dialog: false, loading: false, itemsPerPage: 10, totalItems: 0, serverItems: [],

    allHeaders: [
      { title: 'ID (a_id)', key: 'id' },
      { title: 'Sample (s_id)', key: 'sId' },
      { title: 'Zeit (s_stamp)', key: 'sStamp', width: '160px' },
      { title: 'Name', key: 'sampleName' },
      { title: 'Brutto', key: 'weightBru' },
      { title: 'Netto', key: 'weightNet' },
      { title: 'Box', key: 'boxId' },
      { title: 'Pol', key: 'pol' },
      { title: 'Nat', key: 'nat' },
      { title: 'Aktionen', key: 'actions', sortable: false },
    ],
    selectedHeaders: [],

    editedIndex: -1,
    editedItem: { id: null, sId: '', sStamp: null, pol: '', nat: '', comment: '', sampleName: '', weightBru: 0, weightNet: 0, boxId: '' },
    defaultItem: { id: null, sId: '', sStamp: null, pol: '', nat: '', comment: '', sampleName: '', weightBru: 0, weightNet: 0, boxId: '' },
    rules: { required: v => !!v || 'Pflichtfeld' }
  }),
  computed: { formTitle() { return this.editedIndex === -1 ? 'Neue Analyse' : 'Analyse bearbeiten'; } },

  created() {
    this.selectedHeaders = this.allHeaders;
  },

  methods: {
    formatDateTime(val) {
      return val ? val.toString().replace('T', ' ').substring(0, 19) : '-';
    },
    handleError(error) {
      let msg = "Fehler";
      if (error.response && error.response.data) {
        msg = (typeof error.response.data === 'object') ? Object.values(error.response.data).join(', ') : error.response.data;
      }
      this.snackbarText = msg;
      this.snackbarColor = 'error';
      this.snackbar = true;
    },
    loadItems({page, itemsPerPage, sortBy = []}) {
      this.loading = true;
      const sortKey = sortBy.length ? sortBy[0].key : 'id';
      const sortOrder = sortBy.length ? (sortBy[0].order === 'desc' ? 'desc' : 'asc') : 'asc';
      AnalysisService.getAll(page - 1, itemsPerPage, sortKey, sortOrder)
          .then(res => {
            this.serverItems = res.data.content;
            this.totalItems = res.data.totalElements;
            this.loading = false;
          })
          .catch(this.handleError);
    },
    openDialog() {
      this.editedItem = Object.assign({}, this.defaultItem);
      this.editedIndex = -1;
      this.dialog = true;
    },
    editItem(item) {
      this.editedIndex = this.serverItems.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },
    deleteItem(item) {
      if (confirm('Löschen?')) {
        AnalysisService.delete(item.id)
            .then(() => {
              this.loadItems({
                page: 1,
                itemsPerPage: this.itemsPerPage
              });

              this.snackbarText = "Erfolgreich gelöscht";
              this.snackbarColor = "success";
              this.snackbar = true;
            })
            .catch(this.handleError);
      }
    },
    close() {
      this.dialog = false;
    },
    async save() {
      const {valid} = await this.$refs.form.validate();
      if (!valid) return;
      const req = (this.editedIndex > -1) ? AnalysisService.update(this.editedItem.id, this.editedItem) : AnalysisService.create(this.editedItem);
      req.then(() => {
        this.snackbarText = "Gespeichert";
        this.snackbarColor = "success";
        this.snackbar = true;
        this.close();
        this.loadItems({page: 1, itemsPerPage: 10});
      }).catch(this.handleError);
    }
  },
}
</script>