import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import '@/assets/styles/global.css'

Vue.config.productionTip = false

new Vue({
  router,
  store,
  vuetify, // ◄ O VUETIFY PRECISA SER INJETADO AQUI
  render: h => h(App)
}).$mount('#app')