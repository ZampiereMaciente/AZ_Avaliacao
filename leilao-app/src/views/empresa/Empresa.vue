<template>
  <v-container fluid>
    <v-card class="mx-auto pa-6" max-width="900" color="#f2f2f2">
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

      <FormularioEmpresa 
        :empresa="empresa"
        :podeEditar="podeEditar"
        :isEdicao="isEdicao"
        :erroMensagem="erroMensagem"
        @cancelar="voltar"
        @salvar="salvar"
      />
    </v-card>
  </v-container>
</template>

<script>
import api from '@/api'
import FormularioEmpresa from '@/components/FormularioEmpresa.vue'

export default {
  name: 'EmpresaView',
  components: { FormularioEmpresa },
  props: {
    id: { type: [String, Number], default: null }
  },
  data() {
    return {
      erroMensagem: '',
      podeEditar: !this.$route.params.id, 
      empresa: {
        razaoSocial: '', cnpj: '', telefone: '', email: '', site: '',
        logradouro: '', municipio: '', numero: '', complemento: '',
        bairro: '', cep: '', usuario: '', senha: ''
      }
    }
  },
  computed: {
    isEdicao() {
      return !!this.id
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
        this.empresa.senha = '' // 💡 GARANTE que a tela carregue sem senha visível, ativando o placeholder informativo
      } catch (error) {
        console.error('Erro ao carregar dados:', error)
        this.erroMensagem = 'Erro ao recuperar dados da empresa.'
      }
    },
    voltar() {
      this.$router.push('/empresas')
    },
    async salvar(dadosEmpresa) {
      this.erroMensagem = ''

      const dadosParaEnviar = {
        ...dadosEmpresa,
        cnpj: dadosEmpresa.cnpj.replace(/\D/g, ''),
        telefone: dadosEmpresa.telefone.replace(/\D/g, ''),
        cep: dadosEmpresa.cep.replace(/\D/g, '')
      }

      // 💡 SE FOR EDIÇÃO E A SENHA ESTIVER VAZIA: Removemos o campo antes de enviar para a API
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