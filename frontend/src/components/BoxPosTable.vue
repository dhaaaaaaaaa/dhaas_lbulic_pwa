<template>
  <v-container>
    <v-data-table-server
        v-model:items-per-page="itemsPerPage"
        :headers="selectedHeaders"
        :items="serverItems"
        :items-length="totalItems"
        :loading="loading"
        item-value="id.bposId"
        @update:options="loadItems"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>BoxPos</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="openDialog()">Neu</v-btn>
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

      <template v-slot:item.bid="{ item }">
        {{ item.bid || item.bId || item.id?.bId || '---' }}
      </template>

      <template v-slot:item.bposid="{ item }">
        {{ (item.bposid !== undefined) ? item.bposid : ((item.bposId !== undefined) ? item.bposId : '---') }}
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
                <v-col cols="6">
                  <v-text-field
                      v-model="editedItem.bId"
                      label="Box ID (b_id)"
                      :disabled="editedIndex > -1"
                      :rules="[rules.required]">
                  </v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      v-model.number="editedItem.bposId"
                      label="Pos ID (bpos_id)"
                      type="number"
                      :disabled="editedIndex > -1"
                      :rules="[rules.required]">
                  </v-text-field>
                </v-col>

                <v-col cols="12"><v-text-field v-model="editedItem.sId" label="Sample ID (s_id)" :rules="[rules.required]"></v-text-field></v-col>
                <v-col cols="12"><v-text-field v-model="editedItem.sStamp" label="Sample Time (s_stamp)"></v-text-field></v-col>
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
import BoxPosService from '../services/BoxPosService';

export default {
  data: () => ({
    valid: false,
    snackbar: false, snackbarText: '', snackbarColor: 'success',
    dialog: false, loading: false, itemsPerPage: 10, totalItems: 0, serverItems: [],

    allHeaders: [
      { title: 'b_id', key: 'bid' },
      { title: 'bpos_id', key: 'bposid' },
      { title: 's_id', key: 'sid' },
      { title: 's_stamp', key: 'sstamp' },
      { title: 'Aktionen', key: 'actions', sortable: false },
    ],
    selectedHeaders: [],

    editedIndex: -1,
    editedItem: { bId: '', bposId: null, sId: '', sStamp: null },
    defaultItem: { bId: '', bposId: null, sId: '', sStamp: null },
    rules: { required: v => !!v || 'Pflichtfeld' }
  }),
  computed: { formTitle() { return this.editedIndex === -1 ? 'Neu' : 'Bearbeiten'; } },

  created() {
    this.selectedHeaders = this.allHeaders;
  },

  methods: {
    handleError(error) {
      let msg = "Fehler";
      if (error.response && error.response.data) {
        msg = (typeof error.response.data === 'object') ? Object.values(error.response.data).join(', ') : error.response.data;
      }
      this.snackbarText = msg; this.snackbarColor = 'error'; this.snackbar = true;
    },
    loadItems({ page, itemsPerPage, sortBy = [] }) {
      this.loading = true;
      let sortKey = 'id.bId';
      if (sortBy.length) {
        const key = sortBy[0].key;
        if (key === 'bid') sortKey = 'id.bId';
        else if (key === 'bposid') sortKey = 'id.bposId';
        else sortKey = key;
      }
      const sortOrder = sortBy.length ? (sortBy[0].order === 'desc' ? 'desc' : 'asc') : 'asc';
      BoxPosService.getAll(page - 1, itemsPerPage, sortKey, sortOrder)
          .then(res => { this.serverItems = res.data.content; this.totalItems = res.data.totalElements; this.loading = false; })
          .catch(this.handleError);
    },
    openDialog() { this.editedItem = Object.assign({}, this.defaultItem); this.editedIndex = -1; this.dialog = true; },
    editItem(item) {
      this.editedIndex = this.serverItems.indexOf(item);
      this.editedItem = {
        bId: item.bid || item.bId || item.id?.bId,
        bposId: (item.bposid !== undefined) ? item.bposid : item.bposId,
        sId: item.sid || item.sId,
        sStamp: item.sstamp || item.sStamp
      };
      this.dialog = true;
    },
    deleteItem(item) {
      const bIdDel = item.bid || item.bId || item.id?.bId;
      const bposIdDel = (item.bposid !== undefined) ? item.bposid : item.bposId;

      if (confirm('Löschen?')) {
        BoxPosService.delete(bIdDel, bposIdDel)
            .then(() => {
              this.loadItems({page: 1, itemsPerPage: this.itemsPerPage});

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
      const payload = {
        bId: this.editedItem.bId,
        bposId: this.editedItem.bposId,
        sId: this.editedItem.sId,
        sStamp: this.editedItem.sStamp
      };
      const req = (this.editedIndex > -1) ? BoxPosService.update(payload) : BoxPosService.create(payload);
      req.then(() => {
        this.snackbarText = "Gespeichert";
        this.snackbarColor = "success";
        this.snackbar = true;
        this.close();
        this.loadItems({page: 1, itemsPerPage: this.itemsPerPage});
      }).catch(this.handleError);
    },
  },
}
</script>