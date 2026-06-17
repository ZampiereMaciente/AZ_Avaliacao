<template>
  <v-form ref="form" v-model="valido" lazy-validation>
    <v-row>
      <v-col cols="12" md="6">
        <v-text-field
          v-model="localLeilao.codigo"
          label="Código do Leilão"
          type="number"
          :rules="[v => !!v || 'Código é obrigatório']"
          required
        ></v-text-field>
      </v-col>

      <v-col cols="12" md="6">
        <v-text-field
          v-model="localLeilao.descricao"
          label="Descrição do Leilão"
          :rules="[v => !!v || 'Descrição é obrigatória']"
          required
        ></v-text-field>
      </v-col>

      <v-col cols="12" md="6">
        <v-select
          v-model="localLeilao.vendedorId"
          :items="empresas"
          item-text="razaoSocial"
          item-value="id"
          label="Empresa Vendedora"
          :rules="[v => !!v || 'Empresa vendedora é obrigatória']"
          required
        ></v-select>
      </v-col>

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
      <v-btn color="error" text class="mr-4" @click="$emit('cancelar')">Cancelar</v-btn>
      <v-btn color="primary" :disabled="!valido" @click="dispararSalvar">Salvar</v-btn>
    </v-card-actions>
  </v-form>
</template>

<script>
export default {
  name: 'FormularioLeilao',
  props: {
    leilao: { type: Object, required: true },
    empresas: { type: Array, default: () => [] }
  },
  data: () => ({
    valido: true,
    menuData: false,
    menuHora: false,
    dataSelecionada: '',
    horaSelecionada: '',
    localLeilao: {}
  }),
  computed: {
    dataFormatada() {
      if (!this.dataSelecionada) return ''
      const [ano, mes, dia] = this.dataSelecionada.split('-')
      return `${dia}/${mes}/${ano}`
    }
  },
  watch: {
    leilao: {
      handler(novoValor) {
        this.localLeilao = { ...novoValor }
        
        // Separa inicioPrevisto estruturado da API em Data e Hora independentes
        if (novoValor.inicioPrevisto) {
          const partes = novoValor.inicioPrevisto.split('T')
          if (partes.length === 2) {
            this.dataSelecionada = partes[0]
            this.horaSelecionada = partes[1].substring(0, 5)
          }
        } else {
          this.dataSelecionada = ''
          this.horaSelecionada = ''
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    dispararSalvar() {
      if (this.$refs.form.validate()) {
        // Remonta a estrutura string ISO combinada esperada pelo Java Backend
        const dataEHoraMesclada = `${this.dataSelecionada}T${this.horaSelecionada}:00`
        this.$emit('salvar', { ...this.localLeilao, inicioPrevisto: dataEHoraMesclada })
      }
    }
  }
}
</script>