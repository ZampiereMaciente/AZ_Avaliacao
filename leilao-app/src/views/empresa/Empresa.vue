<template>
  <v-container fluid>
    <v-card class="mx-auto pa-6 "  max-width="900" color=#f2f2f2>
      <v-card-title class="pl-0 pt-0 d-flex justify-space-between align-center">
        <h2>{{ isEdicao ? 'Alterar Empresa' : 'Incluir Empresa' }}</h2>
        
        <v-switch
          v-if="isEdicao"
          v-model="podeEditar"
          label="Habilitar Edição"
          color="primary"
          hide-details
          class="mt-0 pt-0"
        ></v-switch>
      </v-card-title>

      <v-form ref="form" v-model="valid" lazy-validation>
        <v-alert v-if="erroMensagem" type="error" dismissible class="mb-4">
          {{ erroMensagem }}
        </v-alert>

        <v-row>
          <v-col cols="12">
            <h3 class="grey--text text--darken-1 mb-2">Dados Cadastrais</h3>
            <v-divider class="mb-4"></v-divider>
          </v-col>

          <v-col cols="12" sm="8">
            <v-text-field
              v-model="empresa.razaoSocial"
              label="Razão Social*"
              :rules="[v => !!v || 'Razão Social é obrigatória']"
              :readonly="!podeEditar"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" sm="4">
            <v-text-field
              v-model="empresa.cnpj"
              label="CNPJ*"
              :rules="[v => !!v || 'CNPJ é obrigatório']"
              @input="aplicarMascaraCNPJ"
              :readonly="!podeEditar"
              maxlength="18"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" sm="4">
            <v-text-field
              v-model="empresa.telefone"
              label="Telefone*"
              @input="aplicarMascaraTelefone"
              :readonly="!podeEditar"
              maxlength="15"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" sm="4">
            <v-text-field
              v-model="empresa.email"
              label="E-mail*"
              :readonly="!podeEditar"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" sm="4">
            <v-text-field
              v-model="empresa.site"
              label="Site / URL"
              :readonly="!podeEditar"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" class="mt-4">
            <h3 class="grey--text text--darken-1 mb-2">Endereço</h3>
            <v-divider class="mb-4"></v-divider>
          </v-col>

          <v-col cols="12" sm="3">
            <v-text-field
              v-model="empresa.cep"
              label="CEP*"
              @input="aplicarMascaraCEP"
              :readonly="!podeEditar"
              maxlength="9"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" sm="6">
            <v-text-field
              v-model="empresa.logradouro"
              label="Logradouro*"
              :readonly="!podeEditar"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" sm="3">
            <v-text-field
              v-model="empresa.numero"
              label="Número*"
              :readonly="!podeEditar"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" sm="4">
            <v-text-field
              v-model="empresa.complemento"
              label="Complemento"
              :readonly="!podeEditar"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" sm="4">
            <v-text-field
              v-model="empresa.bairro"
              label="Bairro*"
              :readonly="!podeEditar"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" sm="4">
            <v-text-field
              v-model="empresa.municipio"
              label="Município*"
              :readonly="!podeEditar"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" class="mt-4">
            <h3 class="grey--text text--darken-1 mb-2">Acesso ao Sistema</h3>
            <v-divider class="mb-4"></v-divider>
          </v-col>

          <v-col cols="12" sm="6">
            <v-text-field
              v-model="empresa.usuario"
              label="Usuário*"
              :readonly="!podeEditar"
              outlined dense
            ></v-text-field>
          </v-col>

          <v-col cols="12" sm="6">
            <v-text-field
              v-model="empresa.senha"
              label="Senha*"
              type="password"
              :readonly="!podeEditar"
              outlined dense
            ></v-text-field>
          </v-col>
        </v-row>

        <v-card-actions class="pl-0 pr-0 pt-4">
          <v-btn color="error" text @click="voltar">Cancelar</v-btn>
          <v-spacer></v-spacer>
          <v-btn color="primary" :disabled="!valid || !podeEditar" @click="salvar">
            Gravar Dados
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-container>
</template>

<script>
import api from '@/api'

export default {
  name: 'EmpresaForm',
  props: {
    id: {
      type: [String, Number],
      default: null
    }
  },
  data() {
    return {
      valid: true,
      erroMensagem: '',
      // Se tiver ID na URL (Edição), começa como false (bloqueado). Se não tiver (Inclusão), começa true.
      podeEditar: !this.$route.params.id, 
      empresa: {
        razaoSocial: '',
        cnpj: '',
        telefone: '',
        email: '',
        site: '',
        logradouro: '',
        municipio: '',
        numero: '',
        complemento: '',
        bairro: '',
        cep: '',
        usuario: '',
        senha: ''
      }
    }
  },

  computed: {
    isEdicao() {
      return !!this.id
    },
    regrasSenha() {
      if (!this.isEdicao) {
        return [
          v => !!v || 'Senha é obrigatória',
          v => (v && v.length >= 6) || 'A senha deve conter no mínimo 6 caracteres'
        ]
      }
      return []
    }
  },

  created() {
    if (this.isEdicao) {
      this.carregarEmpresa()
    }
  },

  methods: {
    async carregarEmpresa() {
      try {
        const response = await api.get(`/empresas/${this.id}`)
        this.empresa = response.data
        this.empresa.senha = '' // Limpa localmente para edição opcional
        this.aplicarMascaraCNPJ()
        this.aplicarMascaraTelefone()
        this.aplicarMascaraCEP()
      } catch (error) {
        console.error('Erro ao carregar dados:', error)
        this.erroMensagem = 'Erro ao recuperar dados da empresa.'
      }
    },

    aplicarMascaraCNPJ() {
      let valor = this.empresa.cnpj.replace(/\D/g, '')
      if (valor.length > 14) valor = valor.slice(0, 14)
      if (valor.length <= 2) this.empresa.cnpj = valor
      else if (valor.length <= 5) this.empresa.cnpj = `${valor.slice(0, 2)}.${valor.slice(2)}`
      else if (valor.length <= 8) this.empresa.cnpj = `${valor.slice(0, 2)}.${valor.slice(2, 5)}.${valor.slice(5)}`
      else if (valor.length <= 12) this.empresa.cnpj = `${valor.slice(0, 2)}.${valor.slice(2, 5)}.${valor.slice(5, 8)}/${valor.slice(8)}`
      else this.empresa.cnpj = `${valor.slice(0, 2)}.${valor.slice(2, 5)}.${valor.slice(5, 8)}/${valor.slice(8, 12)}-${valor.slice(12)}`
    },

    aplicarMascaraTelefone() {
      let valor = this.empresa.telefone.replace(/\D/g, '')
      if (valor.length > 11) valor = valor.slice(0, 11)
      if (valor.length <= 2) this.empresa.telefone = valor
      else if (valor.length <= 6) this.empresa.telefone = `(${valor.slice(0, 2)}) ${valor.slice(2)}`
      else if (valor.length <= 10) this.empresa.telefone = `(${valor.slice(0, 2)}) ${valor.slice(2, 6)}-${valor.slice(6)}`
      else this.empresa.telefone = `(${valor.slice(0, 2)}) ${valor.slice(2, 7)}-${valor.slice(7)}`
    },

    aplicarMascaraCEP() {
      let valor = this.empresa.cep.replace(/\D/g, '')
      if (valor.length > 8) valor = valor.slice(0, 8)
      if (valor.length <= 5) {
        this.empresa.cep = valor
      } else {
        this.empresa.cep = `${valor.slice(0, 5)}-${valor.slice(5)}`
      }
    },

    voltar() {
      this.$router.push('/empresas')
    },

    async salvar() {
      if (!this.$refs.form.validate()) return
      this.erroMensagem = ''

      // Sanitiza strings removendo formatações para salvar puramente no banco
      const dadosParaEnviar = {
        ...this.empresa,
        cnpj: this.empresa.cnpj.replace(/\D/g, ''),
        telefone: this.empresa.telefone.replace(/\D/g, ''),
        cep: this.empresa.cep.replace(/\D/g, '')
      }

      if (this.isEdicao && !dadosParaEnviar.senha) {
        delete dadosParaEnviar.senha
      }

      try {
        if (this.isEdicao) {
          await api.put(`/empresas/${this.id}`, dadosParaEnviar)
        } else {
          await api.post('/empresas', dadosParaEnviar)
        }
        this.voltar()
      } catch (error) {
        this.erroMensagem = error.response?.data?.message || 'Erro ao processar requisição no servidor.'
      }
    }
  }
}
</script>