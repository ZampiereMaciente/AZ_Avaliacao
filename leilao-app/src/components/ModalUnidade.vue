<template>
    <v-dialog :value="dialog" max-width="500px" persistent>
    <v-card class="pa-4" color="#f2f2f2">
      
      <v-card-title class="px-0 pt-0">
        <h2 class="headline font-weight-bold grey--text text--darken-3">
          {{ titulo }}
        </h2>
      </v-card-title>

      <v-divider class="mb-4"></v-divider>

      <v-card-text class="px-0">
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-row dense>
            <v-col cols="12">
              <v-text-field
                v-model="localItem.nome"
                label="Nome da Unidade*"
                :rules="[v => !!v || 'O nome é obrigatório']"
                outlined
                dense
                hide-details="auto"
                required
              ></v-text-field>
            </v-col>
          </v-row>
        </v-form>
        
        <v-alert v-if="erroMensagem" type="error" dismissible class="mt-4">
          {{ erroMensagem }}
        </v-alert>
      </v-card-text>

      <v-card-actions class="px-0 mt-2">
        <v-btn color="error" text @click="$emit('fechar')">
          Cancelar
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn color="primary" :disabled="!valid" @click="dispararSalvar">
          Gravar Dados
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'ModalUnidade',
  props: {
    dialog: { type: Boolean, required: true },
    titulo: { type: String, required: true },
    item: { type: Object, required: true },
    erroMensagem: { type: String, default: '' }
  },
  data: () => ({
    valid: true,
    localItem: {}
  }),
  watch: {
    // Sincroniza o item recebido da View com a cópia local editável
    item: {
      handler(novoValor) {
        this.localItem = { ...novoValor }
      },
      deep: true,
      immediate: true
    },
    // Limpa a validação visual do formulário sempre que o modal abre/fecha
    dialog(aberto) {
      if (!aberto && this.$refs.form) {
        this.$refs.form.resetValidation()
      }
    }
  },
  methods: {
    dispararSalvar() {
      if (this.$refs.form.validate()) {
        this.$emit('salvar', this.localItem)
      }
    }
  }
}
</script>