<template>
  <v-container fluid>
    <v-card class="pa-4" color="#DCDCDC">
      <v-card-title class="d-flex justify-space-between align-center py-3">
        <h2 class="grey--text text--darken-3 font-weight-bold">Cadastro de Unidades</h2>
        
        <div class="d-flex align-center">
          <v-text-field
            v-model="busca"
            append-icon="mdi-magnify"
            label="Buscar unidade..."
            outlined
            dense
            hide-details
            background-color="#f5f5f5"
            style="max-width: 260px;"
            class="mr-4"
          ></v-text-field>

          <v-btn color="primary" dark @click="novaUnidade">
            Nova Unidade
          </v-btn>
        </div>
      </v-card-title>

      <v-data-table
        :headers="headers"
        :items="unidades"
        :search="busca"
        :loading="loading"
        loading-text="A carregar unidades... Por favor aguarde"
        no-data-text="Nenhuma unidade encontrada ou falha na conexão com o servidor."
        class="elevation-1 tabela-customizada"
      >
        <template v-slot:item.acoes="{ item }">
          <v-icon small class="mr-2" color="blue" @click="editarItem(item)">
            mdi-pencil
          </v-icon>
          <v-icon small color="red" @click="confirmarExclusao(item)">
            mdi-delete
          </v-icon>
        </template>
      </v-data-table>
    </v-card>

    <ModalUnidade
      :dialog="dialog"
      :titulo="formTitle"
      :item="editedItem"
      :erroMensagem="erroMensagem"
      @fechar="fechar"
      @salvar="salvar"
    />

    <v-dialog v-model="dialogConfirmar" max-width="450px" persistent>
      <v-card class="pa-4">
        <v-card-title class="d-flex flex-column align-center justify-center pb-2">
          <v-icon color="warning" size="56" class="mb-2">mdi-alert-circle-outline</v-icon>
          <h3 class="headline font-weight-bold grey--text text--darken-3 text-center width-100">
            Confirmar Exclusão
          </h3>
        </v-card-title>

        <v-card-text class="text-body-1 text-center grey--text text--darken-2 pt-2">
          Tem certeza que deseja eliminar a unidade <br>
          <strong>"{{ unidadeSelecionada?.nome }}"</strong>?
        </v-card-text>

        <v-card-actions class="justify-center pt-4">
          <v-btn color="grey darken-1" text class="px-4 mr-2" @click="dialogConfirmar = false">
            Cancelar
          </v-btn>
          <v-btn color="error" class="px-6" @click="executarExclusao">
            Eliminar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogErro" max-width="450px" persistent>
      <v-card class="pa-4">
        <v-card-title class="d-flex flex-column align-center justify-center pb-2">
          <v-icon color="error" size="56" class="mb-2">mdi-alert-octagon-outline</v-icon>
          <h3 class="headline font-weight-bold grey--text text--darken-3 text-center width-100">
            Não é possível excluir!
          </h3>
        </v-card-title>

        <v-card-text class="text-body-1 text-center grey--text text--darken-2 pt-2">
          {{ mensagemErroExclusao }}
        </v-card-text>

        <v-card-actions class="justify-center pt-4">
          <v-btn color="primary" class="px-6" @click="dialogErro = false">
            Entendi
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import api from '@/api'
import ModalUnidade from '@/components/ModalUnidade.vue'

export default {
  name: 'UnidadesView',
  components: { ModalUnidade },
  data: () => ({
    dialog: false,
    loading: false,
    busca: '',
    erroMensagem: '',
    
    // 💡 NOVAS VARIÁVEIS: Controle dos novos modais
    dialogConfirmar: false,
    unidadeSelecionada: null,
    dialogErro: false,
    mensagemErroExclusao: '',

    headers: [
      { text: 'Nome da Unidade', value: 'nome', sortable: true },
      { text: 'Ações', value: 'acoes', sortable: false, align: 'end' }
    ],
    unidades: [],
    editedIndex: -1,
    editedItem: { id: null, nome: '' },
    defaultItem: { id: null, nome: '' }
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'Nova Unidade' : 'Editar Unidade'
    }
  },

  created() {
    this.listarUnidades()
  },

  methods: {
    async listarUnidades() {
      this.loading = true
      try {
        const response = await api.get('/unidades')
        this.unidades = response.data
      } catch (error) {
        console.error('Erro ao buscar unidades:', error)
        this.unidades = []
      } finally {
        this.loading = false
      }
    },

    novaUnidade() {
      this.editedIndex = -1
      this.editedItem = { ...this.defaultItem }
      this.erroMensagem = ''
      this.dialog = true
    },

    editarItem(item) {
      this.editedIndex = this.unidades.indexOf(item)
      this.editedItem = { ...item }
      this.erroMensagem = ''
      this.dialog = true
    },

    // 💡 PASSO 1: Abre o modal de confirmação
    confirmarExclusao(item) {
      this.unidadeSelecionada = item
      this.dialogConfirmar = true
    },

    // 💡 PASSO 2: Executa a exclusão de fato e trata o erro de integridade se houver
    async executarExclusao() {
      this.dialogConfirmar = false
      this.loading = true
      try {
        await api.delete(`/unidades/${this.unidadeSelecionada.id}`)
        this.listarUnidades() 
      } catch (error) {
        this.mensagemErroExclusao = error.response?.data?.message || 'Não é possível eliminar esta unidade pois ela está vinculada a outros registros.'
        this.dialogErro = true
      } finally {
        this.loading = false
        this.unidadeSelecionada = null
      }
    },

    fechar() {
      this.dialog = false
      this.erroMensagem = ''
      this.$nextTick(() => {
        this.editedItem = { ...this.defaultItem }
        this.editedIndex = -1
      })
    },

    async salvar(itemParaSalvar) {
      this.erroMensagem = ''
      try {
        if (this.editedIndex > -1) {
          await api.put(`/unidades/${itemParaSalvar.id}`, { nome: itemParaSalvar.nome })
        } else {
          await api.post('/unidades', { nome: itemParaSalvar.nome })
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
.width-100 {
  width: 100%;
}
</style>