<template >
  <v-container fluid>
    <v-card class="pa-4" color="#DCDCDC">
      <v-card-title class="d-flex justify-space-between align-center">
        <h2>Consulta de Leilões</h2>
        <v-btn color="primary" dark to="/leilao">
          Novo Leilão
        </v-btn>
      </v-card-title>

      <v-data-table
        :headers="headers"
        :items="leiloes"
        :loading="loading"
        loading-text="Carregando leilões... Por favor aguarde"
        no-data-text="Nenhum leilão encontrado ou falha na conexão com o servidor."
        class="elevation-1"
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
          <v-icon small color="red" @click="deletarLeilao(item)">
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
  name: 'LeiloesView',
  data: () => ({
    loading: false,
    headers: [
      { text: 'ID', value: 'id', align: 'start', sortable: true },
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

    async deletarLeilao(item) {
      if (confirm(`Deseja realmente excluir o leilão com código "${item.codigo}"?`)) {
        try {
          await api.delete(`/leiloes/${item.id}`)
          this.listarLeiloes()
        } catch (error) {
          alert(error.response?.data?.message || 'Erro ao tentar remover o leilão.')
        }
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