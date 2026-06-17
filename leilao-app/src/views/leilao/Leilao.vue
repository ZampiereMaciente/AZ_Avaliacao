<template>
  <v-container>
    <v-card class="pa-6" color="#f2f2f2">
      <v-card-title class="px-0 pt-0">
        <h2>{{ editando ? 'Editar Leilão' : 'Novo Leilão' }}</h2>
      </v-card-title>

      <!-- 💡 FORMULÁRIO COMPONENTIZADO -->
      <FormularioLeilao 
        :leilao="leilao"
        :empresas="empresas"
        @cancelar="voltar"
        @salvar="salvar"
      />
    </v-card>
  </v-container>
</template>

<script>
import api from '@/api'
import FormularioLeilao from '@/components/FormularioLeilao.vue'

export default {
  name: 'LeilaoFormView',
  components: { FormularioLeilao },
  props: {
    id: { type: [String, Number], default: null }
  },
  data: () => ({
    editando: false,
    empresas: [],
    leilao: {
      codigo: '',
      descricao: '',
      vendedorId: null,
      inicioPrevisto: ''
    }
  }),
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
      } catch (error) {
        console.error('Erro ao buscar dados do leilão:', error)
      }
    },
    voltar() {
      this.$router.push('/leiloes')
    },
    async salvar(objetoLeilaoAtualizado) {
      try {
        if (this.editando) {
          await api.put(`/leiloes/${this.id}`, objetoLeilaoAtualizado)
        } else {
          await api.post('/leiloes', objetoLeilaoAtualizado)
        }
        this.voltar()
      } catch (error) {
        console.error('Erro ao salvar o leilão:', error)
        alert(error.response?.data?.message || 'Erro ao salvar o leilão. Verifique os dados.')
      }
    }
  }
}
</script>