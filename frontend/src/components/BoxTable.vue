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
          <v-toolbar-title>Boxen</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn color="primary" variant="elevated" @click="openDialog()">Neu</v-btn>
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

      <template v-slot:item.actions="{ item }">
        <v-icon size="small" class="me-2" @click="editItem(item)">mdi-pencil</v-icon>
        <v-icon size="small" color="red" @click="deleteItem(item)">mdi-delete</v-icon>
      </template>
    </v-data-table-server>

    <v-dialog v-model="dialog" max-width="500px">
      <v-card>
        <v-card-title>{{ formTitle }}</v-card-title>
        <v-card-text>
          <v-container>
            <v-form ref="form" v-model="valid" @submit.prevent>
              <v-row>
                <v-col cols="12">
                  <v-text-field v-model="editedItem.id" label="b_id" :disabled="editedIndex > -1" :rules="[rules.required, rules.idLen]"></v-text-field>
                </v-col>
                <v-col cols="12"><v-text-field v-model="editedItem.name" label="name" :rules="[rules.required]"></v-text-field></v-col>
                <v-col cols="6"><v-text-field v-model.number="editedItem.numMax" label="num_max" type="number" :rules="[rules.required, rules.min1]"></v-text-field></v-col>
                <v-col cols="6"><v-text-field v-model.number="editedItem.type" label="Typ" type="number"></v-text-field></v-col>
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

    <v-snackbar v-model="snackbar" :color="snackbarColor" timeout="4000">
      {{ snackbarText }}
      <template v-slot:actions><v-btn variant="text" @click="snackbar = false">X</v-btn></template>
    </v-snackbar>
  </v-container>
</template>

<script>
import BoxService from '../services/BoxService';

export default {
  data: () => ({
    valid: false,
    snackbar: false, snackbarText: '', snackbarColor: 'success',
    dialog: false, loading: false, itemsPerPage: 10, totalItems: 0, serverItems: [],

    allHeaders: [
      { title: 'b_id', key: 'id' },
      { title: 'name', key: 'name' },
      { title: 'num_max', key: 'numMax' },
      { title: 'type', key: 'type' },
      { title: 'comment', key: 'comment' },
      { title: 'Aktionen', key: 'actions', sortable: false },
    ],
    selectedHeaders: [],

    editedIndex: -1,
    editedItem: { id: '', name: '', numMax: 40, type: 1, comment: '' },
    defaultItem: { id: '', name: '', numMax: 40, type: 1, comment: '' },
    rules: {
      required: value => !!value || 'Pflichtfeld.',
      min1: value => value > 0 || 'Muss > 0 sein.',
      idLen: value => (value && value.length >= 2) || 'ID zu kurz',
    },
  }),
  computed: { formTitle() { return this.editedIndex === -1 ? 'Neue Box' : 'Bearbeiten'; } },

  created() {
    this.selectedHeaders = this.allHeaders;
  },

  methods: {
    showError(error) {
      let msg = "Fehler";
      if (error.response && error.response.data) {
        msg = (typeof error.response.data === 'object') ? Object.values(error.response.data).join(', ') : error.response.data;
      }
      this.snackbarText = msg; this.snackbarColor = 'red'; this.snackbar = true;
    },
    loadItems({ page, itemsPerPage, sortBy = [] }) {
      this.loading = true;
      const sortKey = sortBy.length ? sortBy[0].key : 'id';
      const sortOrder = sortBy.length ? (sortBy[0].order === 'desc' ? 'desc' : 'asc') : 'asc';
      BoxService.getAll(page - 1, itemsPerPage, sortKey, sortOrder).then(response => {
        this.serverItems = response.data.content;
        this.totalItems = response.data.totalElements;
        this.loading = false;
      });
    },
    openDialog() { this.editedItem = Object.assign({}, this.defaultItem); this.editedIndex = -1; this.dialog = true; },
    editItem(item) { this.editedIndex = this.serverItems.indexOf(item); this.editedItem = Object.assign({}, item); this.dialog = true; },
    deleteItem(item) {
      if (confirm('Löschen?')) {
        BoxService.delete(item.id)
            .then(() => {
              this.loadItems({page: 1, itemsPerPage: this.itemsPerPage});
              this.snackbarText = "Erfolgreich gelöscht";
              this.snackbarColor = "success";
              this.snackbar = true;
            })
            .catch(err => this.showError(err));
      }
    },
    close() {
      this.dialog = false;
    },
    async save() {
      const {valid} = await this.$refs.form.validate();
      if (!valid) return;
      const request = this.editedIndex > -1 ? BoxService.update(this.editedItem.id, this.editedItem) : BoxService.create(this.editedItem);
      request.then(() => {
        this.loadItems({page: 1, itemsPerPage: 10});
        this.snackbarText = "Gespeichert!";
        this.snackbarColor = "success";
        this.snackbar = true;
        this.close();
      }).catch(err => {
        this.showError(err);
      });
    },
  },
}
</script>