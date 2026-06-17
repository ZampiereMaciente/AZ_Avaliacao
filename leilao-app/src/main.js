import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import '@/assets/styles/global.css'

Vue.config.productionTip = false

// Ponto de entrada da aplicacao Vue com as devidas injecoes de plugins e frameworks
new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')