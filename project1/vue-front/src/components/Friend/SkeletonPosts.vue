<!-- 個人頁面下半部貼文之骨架屏  -->
<template>
    <div class="row">
      <div class="imageForPost skeleton" v-for="n in 3" :key="n">
        <div class="skeleton-img"></div>
        <div class="details">
          <h2 class="skeleton-title"><span></span></h2>
          <p class="skeleton-text"></p>
          <div class="more">
            <a class="read-more skeleton-read-more"><span></span></a>
            <div class="icon-links">
              <i class="skeleton-icon"></i>
              <i class="skeleton-icon"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  </script>
  
  <style scoped>
  @import '@/assets/personalPage.css';
  .skeleton {
    position: relative;
    overflow: hidden;
    background-color: #e0e0e0;
    border: 1px solid #ccc;
  }
  
  .skeleton-img {
    width: 100%;
    height: 270px;
    background-color: #cfcfcf;
  }
  
  .details {
    padding: 15px;
  }
  
  .skeleton-title,
  .skeleton-text,
  .skeleton-read-more,
  .skeleton-icon {
    background-color: #d8d8d8;
    border-radius: 4px;
  }
  
  .skeleton-title {
    width: 70%;
    height: 20px;
    margin-bottom: 10px;
  }
  
  .skeleton-text {
    width: 90%;
    height: 15px;
    margin-bottom: 10px;
  }
  
  .skeleton-read-more {
    width: 30%;
    height: 15px;
  }
  
  .skeleton-icon {
    width: 20px;
    height: 20px;
    margin-right: 10px;
  }
  </style>
  