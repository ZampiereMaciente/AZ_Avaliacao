<template>
  <v-container fluid>
    <v-card class="pa-4" color="#DCDCDC">
      <v-card-title class="d-flex justify-space-between align-center py-3">
        <h2 class="grey--text text--darken-3 font-weight-bold">Consulta de Empresas</h2>
        
        <div class="d-flex align-center">
          <v-text-field
            v-model="busca"
            append-icon="mdi-magnify"
            label="Buscar empresa..."
            outlined
            dense
            hide-details
            background-color="#f5f5f5"
            style="max-width: 260px;"
            class="mr-4"
          ></v-text-field>

          <v-btn color="primary" dark to="/empresa">
            Nova Empresa
          </v-btn>
        </div>
      </v-card-title>

      <v-data-table
        :headers="headers"
        :items="empresas"
        :search="busca"
        :loading="loading"
        loading-text="A carregar empresas... Por favor aguarde"
        no-data-text="Nenhuma empresa encontrada ou falha na conexão com o servidor."
        class="elevation-1 tabela-customizada"
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
          <!-- 💡 ALTERADO: Agora passa o item para abrir o nosso modal customizado -->
          <v-icon small color="red" @click="confirmarExclusao(item)">
            mdi-delete
          </v-icon>
        </template>
      </v-data-table>
    </v-card>

    <!-- 💡 NOVO: Modal de CONFIRMAÇÃO de exclusão personalizado (Substitui o confirm nativo) -->
    <v-dialog v-model="dialogConfirmar" max-width="450px" persistent>
      <v-card class="pa-4">
        <v-card-title class="d-flex flex-column align-center justify-center pb-2">
          <v-icon color="warning" size="56" class="mb-2">mdi-alert-circle-outline</v-icon>
          <h3 class="headline font-weight-bold grey--text text--darken-3 text-center width-100">
            Confirmar Exclusão
          </h3>
        </v-card-title>

        <v-card-text class="text-body-1 text-center grey--text text--darken-2 pt-2">
          Tem certeza que deseja excluir a empresa <br>
          <strong>"{{ empresaSelecionada?.razaoSocial }}"</strong>?
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

    <!-- 💡 MANTIDO: Modal de AVISO DE ERRO de exclusão (Substitui o alert nativo) -->
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
  name: 'EmpresasView',
  data: () => ({
    loading: false,
    busca: '',
    
    // 💡 NOVAS VARIÁVEIS: Controle do modal de confirmação
    dialogConfirmar: false,
    empresaSelecionada: null,

    // Variáveis do modal de erro
    dialogErro: false,
    mensagemErroExclusao: '',

    headers: [
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

    // 💡 PASSO 1: Apenas guarda a empresa clicada e abre o modal de confirmação
    confirmarExclusao(item) {
      this.empresaSelecionada = item
      this.dialogConfirmar = true
    },

    // 💡 PASSO 2: Executa a exclusão de fato ao clicar no botão "Excluir" do modal
    async executarExclusao() {
      this.dialogConfirmar = false // Fecha o modal de pergunta
      this.loading = true
      
      try {
        await api.delete(`/empresas/${this.empresaSelecionada.id}`)
        this.listarEmpresas()
      } catch (error) {
        this.mensagemErroExclusao = error.response?.data?.message || 'Não foi possível remover a empresa devido a vínculos ativos no sistema.'
        this.dialogErro = true
      } finally {
        this.loading = false
        this.empresaSelecionada = null // Limpa a referência
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
.row-gap-2 {
  row-gap: 8px;
}
.width-100 {
  width: 100%;
}
</style>