<template>
  <v-container fluid>
    <v-card class="pa-4" color="#DCDCDC">
      <v-card-title class="d-flex justify-space-between align-center py-3">
        <h2 class="grey--text text--darken-3 font-weight-bold">Consulta de Leilões</h2>
        
        <div class="d-flex align-center">
          <v-text-field
            v-model="busca"
            append-icon="mdi-magnify"
            label="Buscar leilão..."
            outlined
            dense
            hide-details
            background-color="#f5f5f5"
            style="max-width: 260px;"
            class="mr-4"
          ></v-text-field>

          <v-btn color="primary" dark to="/leilao">
            Novo Leilão
          </v-btn>
        </div>
      </v-card-title>

      <v-data-table
        :headers="headers"
        :items="leiloes"
        :search="busca"
        :loading="loading"
        loading-text="Carregando leilões... Por favor aguarde"
        no-data-text="Nenhum leilão encontrado ou falha na conexão com o servidor."
        class="elevation-1 tabela-customizada"
      >
        <template v-slot:item.totalLeilao="{ item }">
          {{ formatarMoeda(item.totalLeilao) }}
        </template>

        <template v-slot:item.inicioPrevisto="{ item }">
          {{ formatarData(item.inicioPrevisto) }}
        </template>

        <template v-slot:item.acoes="{ item }">
          <v-icon small color="blue" class="mr-2" @click="editarLeilao(item.id)">
            mdi-pencil
          </v-icon>
          <v-icon small color="red" @click="confirmarExclusao(item)">
            mdi-delete
          </v-icon>
        </template>
      </v-data-table>
    </v-card>

    <v-dialog v-model="dialogConfirmar" max-width="450px" persistent>
      <v-card class="pa-4">
        <v-card-title class="d-flex flex-column align-center justify-center pb-2">
          <v-icon color="warning" size="56" class="mb-2">mdi-alert-circle-outline</v-icon>
          <h3 class="headline font-weight-bold grey--text text--darken-3 text-center width-100">
            Confirmar Exclusão
          </h3>
        </v-card-title>

        <v-card-text class="text-body-1 text-center grey--text text--darken-2 pt-2">
          Deseja realmente excluir o leilão com código <br>
          <strong>"{{ leilaoSelecionado?.codigo }}"</strong>?
        </v-card-text>

        <v-card-actions class="justify-center pt-4">
          <v-btn color="grey darken-1" text class="px-4 mr-2" @click="dialogConfirmar = false">
            Cancelar
          </v-btn>
          <v-btn color="error" class="px-6" @click="executarExclusao">
            Excluir
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

export default {
  name: 'LeiloesView',
  data: () => ({
    loading: false,
    busca: '',
    
    // 💡 NOVAS VARIÁVEIS: Controle dos novos modais
    dialogConfirmar: false,
    leilaoSelecionado: null,
    dialogErro: false,
    mensagemErroExclusao: '',

    headers: [
      { text: 'Código', value: 'codigo', sortable: true },
      { text: 'Descrição', value: 'descricao', sortable: true },
      { text: 'Vendedor', value: 'vendedorRazaoSocial', sortable: true },
      { text: 'Início Previsto', value: 'inicioPrevisto', sortable: true },
      { text: 'Total Geral', value: 'totalLeilao', sortable: true },
      { text: 'Ações', value: 'acoes', sortable: false, align: 'end' }
    ],
    leiloes: []
  }),

  created() {
    this.listarLeiloes()
  },

  methods: {
    async listarLeiloes() {
      this.loading = true
      try {
        const response = await api.get('/leiloes')
        this.leiloes = response.data
      } catch (error) {
        console.error('Erro ao buscar leilões:', error)
      } finally {
        this.loading = false
      }
    },

    editarLeilao(id) {
      this.$router.push(`/leilao/${id}`)
    },

    // 💡 PASSO 1: Abre o modal de confirmação
    confirmarExclusao(item) {
      this.leilaoSelecionado = item
      this.dialogConfirmar = true
    },

    // 💡 PASSO 2: Executa a exclusão de fato na API
    async executarExclusao() {
      this.dialogConfirmar = false
      this.loading = true
      try {
        await api.delete(`/leiloes/${this.leilaoSelecionado.id}`)
        this.listarLeiloes()
      } catch (error) {
        this.mensagemErroExclusao = error.response?.data?.message || 'Erro ao tentar remover o leilão devido a dependências ativas.'
        this.dialogErro = true
      } finally {
        this.loading = false
        this.leilaoSelecionado = null
      }
    },

    formatarMoeda(valor) {
      if (!valor) return 'R$ 0,00'
      return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor)
    },

    formatarData(dataIso) {
      if (!dataIso) return ''
      const data = new Date(dataIso)
      return data.toLocaleString('pt-BR')
    }
  }
}
</script>

<style scoped>
.width-100 {
  width: 100%;
}
</style>