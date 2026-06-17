<template>
  <v-container class="fill-height bg-imagem" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="6" class="text-center">
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'HomeView'
}
</script>

<style scoped>
/* Configuracaes de layout e estilizacao da imagem de fundo da Home */
.fill-height {
  min-height: calc(100vh - 100px);
}

.bg-imagem {
  background-image: url('../assets/backgroundaz.png'); 
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.shadow-text {
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.8);
}
</style>