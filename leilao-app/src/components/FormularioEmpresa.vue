<template>
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
          v-model="localEmpresa.razaoSocial"
          label="Razão Social*"
          :rules="[v => !!v || 'Razão Social é obrigatória']"
          :readonly="!podeEditar"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" sm="4">
        <v-text-field
          v-model="localEmpresa.cnpj"
          label="CNPJ*"
          :rules="regrasCNPJ"
          @input="aplicarMascaraCNPJ"
          :readonly="!podeEditar"
          maxlength="18"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" sm="4">
        <v-text-field
          v-model="localEmpresa.telefone"
          label="Telefone*"
          :rules="regrasTelefone"
          @input="aplicarMascaraTelefone"
          :readonly="!podeEditar"
          maxlength="15"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" sm="4">
        <v-text-field
          v-model="localEmpresa.email"
          label="E-mail*"
          :rules="regrasEmail"
          :readonly="!podeEditar"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" sm="4">
        <v-text-field
          v-model="localEmpresa.site"
          label="Site / URL"
          :readonly="!podeEditar"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" class="mt-4">
        <h3 class="grey--text text--darken-1 mb-2">Endereço</h3>
        <v-divider class="mb-4"></v-divider>
      </v-col>

      <v-col cols="12" sm="3">
        <v-text-field
          v-model="localEmpresa.cep"
          label="CEP"
          @input="aplicarMascaraCEP"
          :readonly="!podeEditar"
          maxlength="9"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" sm="6">
        <v-text-field
          v-model="localEmpresa.logradouro"
          label="Logradouro"
          :readonly="!podeEditar"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" sm="3">
        <v-text-field
          v-model="localEmpresa.numero"
          label="Número"
          :readonly="!podeEditar"
          outlined dense
        ></v-text-field>
      </v-col>

      <v-col cols="12" sm="4">
        <v-text-field
          v-model="localEmpresa.complemento"
          label="Complemento"
          :readonly="!podeEditar"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" sm="4">
        <v-text-field
          v-model="localEmpresa.bairro"
          label="Bairro"
          :readonly="!podeEditar"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" sm="4">
        <v-text-field
          v-model="localEmpresa.municipio"
          label="Município"
          :readonly="!podeEditar"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" class="mt-4">
        <h3 class="grey--text text--darken-1 mb-2">Acesso ao Sistema</h3>
        <v-divider class="mb-4"></v-divider>
      </v-col>

      <v-col cols="12" sm="6">
        <!-- 💡 ALTERADO: Adicionado regras de validação obrigatória e o asterisco -->
        <v-text-field
          v-model="localEmpresa.usuario"
          label="Usuário*"
          :rules="[v => !!v || 'Usuário é obrigatório']"
          :readonly="!podeEditar"
          outlined dense
          hide-details="auto"
        ></v-text-field>
      </v-col>

      <v-col cols="12" sm="6">
        <!-- 💡 ALTERADO: Removido asterisco do label por ser opcional -->
        <v-text-field
          v-model="localEmpresa.senha"
          :label="isEdicao ? 'Nova Senha' : 'Senha'"
          :placeholder="isEdicao ? 'Deixe em branco para manter a atual' : 'Opcional'"
          :type="mostrarSenha ? 'text' : 'password'"
          :append-icon="mostrarSenha ? 'mdi-eye' : 'mdi-eye-off'"
          :rules="regrasSenha"
          :readonly="!podeEditar"
          outlined dense
          hide-details="auto"
          @click:append="mostrarSenha = !mostrarSenha"
        ></v-text-field>
      </v-col>
     
    </v-row>

    <v-card-actions class="pl-0 pr-0 pt-4">
      <v-btn color="error" text @click="$emit('cancelar')">Cancelar</v-btn>
      <v-spacer></v-spacer>
      <v-btn color="primary" :disabled="!valid || !podeEditar" @click="dispararSalvar">
        Gravar Dados
      </v-btn>
    </v-card-actions>
  </v-form>
</template>

<script>
export default {
  name: 'FormularioEmpresa',
  props: {
    empresa: { type: Object, required: true },
    podeEditar: { type: Boolean, default: true },
    isEdicao: { type: Boolean, default: false },
    erroMensagem: { type: String, default: '' }
  },
  data: () => ({
    valid: true,
    localEmpresa: {},
    mostrarSenha: false
  }),
  computed: {
    // 💡 MODIFICADO: A senha agora é opcional tanto na criação quanto na edição.
    // Ela só exige tamanho mínimo SE o usuário resolver digitar algo.
    regrasSenha() {
      return [
        v => !v || v.length >= 6 || 'A senha deve conter no mínimo 6 caracteres'
      ]
    },

    regrasCNPJ() {
      return [
        v => !!v || 'CNPJ é obrigatório',
        v => (v && v.replace(/\D/g, '').length === 14) || 'CNPJ deve conter exatamente 14 números'
      ]
    },
    regrasEmail() {
      return [
        v => !!v || 'E-mail é obrigatório',
        v => /.+@.+\..+/.test(v) || 'Insira um e-mail válido (exemplo@dominio.com)'
      ]
    },
    regrasTelefone() {
      return [
        v => !!v || 'Telefone é obrigatório',
        v => (v && v.replace(/\D/g, '').length >= 10) || 'Telefone inválido (mínimo 10 dígitos)'
      ]
    }
  },
  watch: {
    empresa: {
      handler(novoValor) {
        this.localEmpresa = { ...novoValor }
        
        this.$nextTick(() => {
          if (this.localEmpresa.cnpj) {
            this.aplicarMascaraCNPJ()
          }
          if (this.localEmpresa.telefone) {
            this.aplicarMascaraTelefone()
          }
          if (this.localEmpresa.cep) {
            this.aplicarMascaraCEP()
          }
        })
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    aplicarMascaraCNPJ() {
      let valor = this.localEmpresa.cnpj.replace(/\D/g, '')
      if (valor.length > 14) valor = valor.slice(0, 14)
      if (valor.length <= 2) this.localEmpresa.cnpj = valor
      else if (valor.length <= 5) this.localEmpresa.cnpj = `${valor.slice(0, 2)}.${valor.slice(2)}`
      else if (valor.length <= 8) this.localEmpresa.cnpj = `${valor.slice(0, 2)}.${valor.slice(2, 5)}.${valor.slice(5)}`
      else if (valor.length <= 12) this.localEmpresa.cnpj = `${valor.slice(0, 2)}.${valor.slice(2, 5)}.${valor.slice(5, 8)}/${valor.slice(8)}`
      else this.localEmpresa.cnpj = `${valor.slice(0, 2)}.${valor.slice(2, 5)}.${valor.slice(5, 8)}/${valor.slice(8, 12)}-${valor.slice(12)}`
    },
    aplicarMascaraTelefone() {
      let valor = this.localEmpresa.telefone.replace(/\D/g, '')
      if (valor.length > 11) valor = valor.slice(0, 11)
      if (valor.length <= 2) this.localEmpresa.telefone = valor
      else if (valor.length <= 6) this.localEmpresa.telefone = `(${valor.slice(0, 2)}) ${valor.slice(2)}`
      else if (valor.length <= 10) this.localEmpresa.telefone = `(${valor.slice(0, 2)}) ${valor.slice(2, 6)}-${valor.slice(6)}`
      else this.localEmpresa.telefone = `(${valor.slice(0, 2)}) ${valor.slice(2, 7)}-${valor.slice(7)}`
    },
    aplicarMascaraCEP() {
      let valor = this.localEmpresa.cep.replace(/\D/g, '')
      if (valor.length > 8) valor = valor.slice(0, 8)
      if (valor.length <= 5) this.localEmpresa.cep = valor
      else this.localEmpresa.cep = `${valor.slice(0, 5)}-${valor.slice(5)}`
    },
    dispararSalvar() {
      if (this.$refs.form.validate()) {
        this.$emit('salvar', this.localEmpresa)
      }
    }
  }
}
</script>