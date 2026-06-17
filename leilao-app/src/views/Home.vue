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
/* Centraliza verticalmente o container na tela */
.fill-height {
  min-height: calc(100vh - 100px);
}

/* 🛠️ ESTILIZAÇÃO DO FUNDO DO SITE COM A SUA IMAGEM DE ASSETS */
.bg-imagem {
  /* ATENÇÃO: Substitua 'fundo.jpg' pelo nome e extensão exata da sua imagem que está em assets */
  background-image: url('../assets/backgroundaz.png'); 
  background-size: cover;        /* Faz a imagem cobrir toda a tela sem distorcer */
  background-position: center;   /* Centraliza o fundo na tela */
  background-repeat: no-repeat;  /* Impede que a imagem fique se repetindo */
}

/* Pequena sombra no rodapé para garantir leitura caso o fundo seja claro */
.shadow-text {
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.8);
}
</style>