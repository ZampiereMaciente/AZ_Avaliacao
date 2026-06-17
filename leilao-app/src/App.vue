<template>
  <v-app>
    <v-app-bar app color="primary" dark dense>
      <v-toolbar-title><img class="pt-2" width="130" src="./assets/az3.png"></v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text to="/">
  <v-icon left>mdi-home</v-icon>
    Home
  </v-btn>

  <v-btn text to="/unidades">Unidades</v-btn>
  <v-btn text to="/empresas">Empresas</v-btn>
  <v-btn text to="/leiloes">Leilões</v-btn>
      
    </v-app-bar>

    <v-main>
      <v-container fluid class="mt-4">
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>

<style>
/* Estilos globais se necessários */
body {
  background-color: #f5f5f5;
}
</style>
