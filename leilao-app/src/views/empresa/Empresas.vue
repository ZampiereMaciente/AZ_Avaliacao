<template >
  <v-container fluid>
    <v-card class="pa-4" color="#DCDCDC">
      <v-card-title class="d-flex justify-space-between align-center">
        <h2>Consulta de Empresas</h2>
        
        <v-btn color="primary" dark to="/empresa">
          Nova Empresa
        </v-btn>
      </v-card-title>

      <v-data-table
        :headers="headers"
        :items="empresas"
        :loading="loading"
        loading-text="A carregar empresas... Por favor aguarde"
        no-data-text="Nenhuma empresa encontrado ou falha na conexão com o servidor."
        class="elevation-1"
      >
        <template v-slot:item.cnpj="{ item }">
          {{ formatarCNPJ(item.cnpj) }}
        </template>

        <template v-slot:item.telefone="{ item }">
          {{ formatarTelefone(item.telefone) }}
        </template>

        <template v-slot:item.acoes="{ item }">
          <v-icon small color="blue" class="mr-2" @click="editarEmpresa(item.id)">
            mdi-pencil
          </v-icon>
          <v-icon small color="red" @click="deletarEmpresa(item)">
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
  name: 'EmpresasView',
  data: () => ({
    loading: false,
    headers: [
      { text: 'ID', value: 'id', align: 'start', sortable: true },
      { text: 'CNPJ', value: 'cnpj', sortable: true },
      { text: 'Razão Social', value: 'razaoSocial', sortable: true },
      { text: 'Telefone', value: 'telefone', sortable: true },
      { text: 'E-mail', value: 'email', sortable: true },
      { text: 'Ações', value: 'acoes', sortable: false, align: 'end' }
    ],
    empresas: []
  }),

  created() {
    this.listarEmpresas()
  },

  methods: {
    async listarEmpresas() {
      this.loading = true
      try {
        const response = await api.get('/empresas')
        this.empresas = response.data
      } catch (error) {
        console.error('Erro ao buscar empresas:', error)
      } finally {
        this.loading = false
      }
    },

    editarEmpresa(id) {
      this.$router.push(`/empresa/${id}`)
    },

    async deletarEmpresa(item) {
      if (confirm(`Tem certeza que deseja excluir a empresa "${item.razaoSocial}"?`)) {
        try {
          await api.delete(`/empresas/${item.id}`)
          this.listarEmpresas() // Atualiza a grid
        } catch (error) {
          // Exibe erro amigável lançado pelo seu DeleteConflictException do Java
          alert(error.response?.data?.message || 'Erro ao tentar remover a empresa.')
        }
      }
    },

    formatarCNPJ(cnpj) {
      if (!cnpj) return ''
      return cnpj.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, "$1.$2.$3/$4-$5")
    },

    formatarTelefone(tel) {
      if (!tel) return ''
      if (tel.length === 11) {
        return tel.replace(/^(\d{2})(\d{5})(\d{4})$/, "($1) $2-$3")
      }
      return tel.replace(/^(\d{2})(\d{4})(\d{4})$/, "($1) $2-$3")
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