<template>
  <v-container>
    <v-card class="pa-6" color="#f2f2f2">
      <v-card-title>
        <h2>{{ editando ? 'Editar Leilão' : 'Novo Leilão' }}</h2>
      </v-card-title>

      <v-form ref="form" v-model="valido" lazy-validation>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="leilao.codigo"
              label="Código do Leilão"
              type="number"
              :rules="[v => !!v || 'Código é obrigatório']"
              required
            ></v-text-field>
          </v-col>

          <v-col cols="12" md="6">
            <v-text-field
              v-model="leilao.descricao"
              label="Descrição do Leilão"
              :rules="[v => !!v || 'Descrição é obrigatória']"
              required
            ></v-text-field>
          </v-col>

          <v-col cols="12" md="6">
            <v-select
              v-model="leilao.vendedorId"
              :items="empresas"
              item-text="razaoSocial"
              item-value="id"
              label="Empresa Vendedora"
              :rules="[v => !!v || 'Empresa vendedora é obrigatória']"
              required
            ></v-select>
          </v-col>

          <!-- 💡 CAMPO DE DATA SEPARADO -->
          <v-col cols="12" md="3">
            <v-menu
              v-model="menuData"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              max-width="290px"
              min-width="290px"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="dataFormatada"
                  label="Data de Início"
                  prepend-icon="mdi-calendar"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                  :rules="[v => !!v || 'Data é obrigatória']"
                  required
                ></v-text-field>
              </template>
              <v-date-picker
                v-model="dataSelecionada"
                no-title
                @input="menuData = false"
                locale="pt-br"
              ></v-date-picker>
            </v-menu>
          </v-col>

          <!-- 💡 CAMPO DE HORA SEPARADO -->
          <v-col cols="12" md="3">
            <v-menu
              v-model="menuHora"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              max-width="290px"
              min-width="290px"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="horaSelecionada"
                  label="Hora de Início"
                  prepend-icon="mdi-clock-time-four-outline"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                  :rules="[v => !!v || 'Hora é obrigatória']"
                  required
                ></v-text-field>
              </template>
              <v-time-picker
                v-if="menuHora"
                v-model="horaSelecionada"
                full-width
                format="24hr"
                @click:minute="menuHora = false"
              ></v-time-picker>
            </v-menu>
          </v-col>
        </v-row>

        <v-card-actions class="px-0 mt-4">
          <v-btn color="error" text class="mr-4" to="/leiloes">Cancelar</v-btn>
          <v-btn color="primary" :disabled="!valido" @click="salvar">Salvar</v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-container>
</template>

<script>
import api from '@/api'

export default {
  name: 'LeilaoForm',
  props: ['id'],
  data: () => ({
    valido: true,
    editando: false,
    empresas: [],
    
    // Controles dos seletores de data/hora
    menuData: false,
    menuHora: false,
    dataSelecionada: '', // Armazena YYYY-MM-DD
    horaSelecionada: '', // Armazena HH:mm

    leilao: {
      codigo: '',
      descricao: '',
      vendedorId: null,
      inicioPrevisto: ''
    }
  }),

  computed: {
    // Exibe a data no formato brasileiro (DD/MM/YYYY) para o usuário
    dataFormatada() {
      if (!this.dataSelecionada) return ''
      const [ano, mes, dia] = this.dataSelecionada.split('-')
      return `${dia}/${mes}/${ano}`
    }
  },

  async created() {
    await this.carregarEmpresas()
    if (this.id) {
      this.editando = true
      await this.carregarLeilao()
    }
  },

  methods: {
    async carregarEmpresas() {
      try {
        const response = await api.get('/empresas')
        this.empresas = response.data
      } catch (error) {
        console.error('Erro ao carregar empresas:', error)
      }
    },

    async carregarLeilao() {
      try {
        const response = await api.get(`/leiloes/${this.id}`)
        this.leilao = response.data

        // 💡 TRATAMENTO PARA EDIÇÃO: Separa o inicioPrevisto vindo da API em Data e Hora
        if (this.leilao.inicioPrevisto) {
          // Trata formatos ISO (Ex: 2026-06-16T23:05:00)
          const partes = this.leilao.inicioPrevisto.split('T')
          if (partes.length === 2) {
            this.dataSelecionada = partes[0]
            this.horaSelecionada = partes[1].substring(0, 5) // Pega apenas HH:mm
          }
        }
      } catch (error) {
        console.error('Erro ao buscar dados do leilão:', error)
      }
    },

    async salvar() {
      if (this.$refs.form.validate()) {
        try {
          // 💡 JUNÇÃO DOS CAMPOS ANTES DE ENVIAR PARA O BACKEND
          // Une a data e a hora no formato ISO aceito pelo LocalDateTime do Java: YYYY-MM-DDTHH:mm:00
          this.leilao.inicioPrevisto = `${this.dataSelecionada}T${this.horaSelecionada}:00`

          if (this.editando) {
            await api.put(`/leiloes/${this.id}`, this.leilao)
          } else {
            await api.post('/leiloes', this.leilao)
          }
          this.$router.push('/leiloes')
        } catch (error) {
          console.error('Erro ao salvar o leilão:', error)
          alert(error.response?.data?.message || 'Erro ao salvar o leilão. Verifique os dados.')
        }
      }
    }
  }
}
</script>