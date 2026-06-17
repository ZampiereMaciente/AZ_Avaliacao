<template>
  <v-container fluid>
    <v-card class="pa-4" color="#DCDCDC">
      <v-card-title class="d-flex justify-space-between align-center">
        <h2>Cadastro de Unidades</h2>
        
        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark v-bind="attrs" v-on="{ on }" @click="novaUnidade">
              Nova Unidade
            </v-btn>
          </template>
          
          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-form ref="form" v-model="valid">
                  <v-text-field
                    v-model="editedItem.nome"
                    label="Nome da Unidade*"
                    :rules="[v => !!v || 'O nome é obrigatório']"
                    required
                  ></v-text-field>
                </v-form>
              </v-container>
              <v-alert v-if="erroMensagem" type="error" dismissible class="mt-2">
                {{ erroMensagem }}
              </v-alert>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="fechar">Cancelar</v-btn>
              <v-btn color="blue darken-1" text :disabled="!valid" @click="salvar">Salvar</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-card-title>

      <v-data-table
        :headers="headers"
        :items="unidades"
        :loading="loading"
        loading-text="A carregar unidades... Por favor aguarde"
        no-data-text="Nenhuma unidade encontrado ou falha na conexão com o servidor."
        class="elevation-1"
      >
        <template v-slot:item.acoes="{ item }">
          <v-icon small class="mr-2" color="blue" @click="editarItem(item)">
            mdi-pencil
          </v-icon>
          <v-icon small color="red" @click="deletarItem(item)">
            mdi-delete
          </v-icon>
        </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<script>
import api from '@/api'

export default {
  name: 'UnidadesView',
  data: () => ({
    dialog: false,
    loading: false,
    valid: true,
    erroMensagem: '',
    headers: [
      { text: 'ID', value: 'id', align: 'start', sortable: true },
      { text: 'Nome da Unidade', value: 'nome', sortable: true },
      { text: 'Ações', value: 'acoes', sortable: false, align: 'end' }
    ],
    unidades: [],
    editedIndex: -1,
    editedItem: {
      id: null,
      nome: ''
    },
    defaultItem: {
      id: null,
      nome: ''
    }
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'Nova Unidade' : 'Editar Unidade'
    }
  },

  watch: {
    dialog(val) {
      val || this.fechar()
    }
  },

  created() {
    this.listarUnidades()
  },

  methods: {
    // READ: GET /unidades
    async listarUnidades() {
      this.loading = true
      try {
        const response = await api.get('/unidades')
        this.unidades = response.data
      } catch (error) {
        console.error('Erro ao buscar unidades:', error)
        this.unidades = [] // Garante que zera se a API falhar
      } finally {
        this.loading = false
      }
    },

    novaUnidade() {
      this.editedIndex = -1
      this.editedItem = Object.assign({}, this.defaultItem)
      this.erroMensagem = ''
      this.dialog = true // 💡 CORRIGIDO: Adicionado para abrir o modal ao clicar em Novo
    },

    editarItem(item) {
      this.editedIndex = this.unidades.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.erroMensagem = ''
      this.dialog = true
    },

    // DELETE: DELETE /unidades/:id
    async deletarItem(item) {
      if (confirm(`Tem a certeza que deseja eliminar a unidade "${item.nome}"?`)) {
        try {
          await api.delete(`/unidades/${item.id}`)
          this.listarUnidades() 
        } catch (error) {
          alert(error.response?.data?.message || 'Erro ao eliminar unidade.')
        }
      }
    },

    fechar() {
      this.dialog = false
      this.erroMensagem = ''
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    // CREATE / UPDATE: POST e PUT
    async salvar() {
      if (!this.$refs.form.validate()) return

      this.erroMensagem = ''
      try {
        if (this.editedIndex > -1) {
          await api.put(`/unidades/${this.editedItem.id}`, { nome: this.editedItem.nome })
        } else {
          await api.post('/unidades', { nome: this.editedItem.nome })
        }
        this.listarUnidades()
        this.fechar()
      } catch (error) {
        this.erroMensagem = error.response?.data?.message || 'Ocorreu um erro ao guardar os dados.'
      }
    }
  }
}
</script>

<style scoped>
::v-deep .v-data-table tbody tr:nth-of-type(odd) {
  background-color: #f2f2f2;
}
::v-deep .v-data-table tbody tr:hover {
  background-color: #e6e6e6 !important;
}
</style>

<style scoped>
/* 💡 Altera o tamanho da fonte do texto dos cabeçalhos */
::v-deep .v-data-table thead th {
  font-size: 16px !important;       /* Altere o valor (ex: 14px, 18px) para o tamanho que desejar */
  font-weight: bold !important;     /* Opcional: Deixa o texto em negrito */
}

/* Mantém os seus estilos anteriores abaixo */
::v-deep .v-data-table tbody tr:nth-of-type(odd) {
  background-color: #f2f2f2;
}
::v-deep .v-data-table tbody tr:hover {
  background-color: #e6e6e6 !important;
}
</style>