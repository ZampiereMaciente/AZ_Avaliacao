import Vue from 'vue';
import Vuetify from 'vuetify';
import 'vuetify/dist/vuetify.min.css';

Vue.use(Vuetify);

// Configuracao do plugin Vuetify para o layout UI e customizacao do tema de cores
export default new Vuetify({
  theme: {
    themes: {
      light: {
        primary: '#072c76',
        secondary: '#424242',
        accent: '#82B1FF',
        error: '#FF5252',
        info: '#2196F3',
        success: '#4CAF50',
        warning: '#FFC107',
      },
    },
  },
});