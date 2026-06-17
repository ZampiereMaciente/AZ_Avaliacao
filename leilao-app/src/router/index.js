import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Home.vue'

Vue.use(VueRouter)

// Definicao das rotas para navegacao entre as telas da aplicacao
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/unidades',
    name: 'unidades',
    component: () => import('../views/unidade/Unidades.vue')
  },
  {
    path: '/empresas',
    name: 'empresas',
    component: () => import('../views/empresa/Empresas.vue')
  },
  {
    path: '/empresa',
    name: 'nova-empresa',
    component: () => import('../views/empresa/Empresa.vue')
  },
  {
    path: '/empresa/:id',
    name: 'editar-empresa',
    component: () => import('../views/empresa/Empresa.vue'),
    props: true
  },
  {
    path: '/leiloes',
    name: 'leiloes',
    component: () => import('../views/leilao/Leiloes.vue')
  },
  {
    path: '/leilao',
    name: 'novo-leilao',
    component: () => import('../views/leilao/Leilao.vue') 
  },
  {
    path: '/leilao/:id',
    name: 'editar-leilao',
    component: () => import('../views/leilao/Leilao.vue'), 
    props: true
  }
]

// Instanciacao e configuracao do roteador VueRouter
const router = new VueRouter({
  routes 
})

export default router