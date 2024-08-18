<template>
    <div class="app-container">
      <Navigation></Navigation>
      <br>
      <br>
      <br>
      <RouterView></RouterView>
      <!-- 顯示路由視圖的內容 -->
    </div>
  </template>
    
  <script setup>
  import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import Navigation from './views/Navigation.vue';
  </script>
    
  <style>

  .app-container {
    position: relative;
    min-height: 100vh; 
    overflow: hidden;
    padding: 0;
  }
  
  body::before {
    content: "";
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    
    background-size: cover;
    background-position: center;
    background-attachment: fixed;
    z-index: -1;
  }
  
  body {
    margin: 0;
    padding: 0;
  }
  </style>
  