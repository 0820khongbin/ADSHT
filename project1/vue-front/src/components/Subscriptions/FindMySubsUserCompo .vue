<template>
  <div class="nft" v-for=" product in productA">
    <div class='main' @click="goToProfile(product.ServiceCreaterId)">
        <img class='tokenImage' :src="product.ServiceCreaterImg || defaultImage" :alt="product.ServiceCreater" loading="lazy"/>
        <h2 style="text-align: center;">{{ product.ServiceName }}</h2>
        <hr>
        <p class='description'>會員等級:
          {{  product.ServiceLevel  }}</p>
        <div class='tokenInfo'>
          <div class="price">
          </div>
          <div class="duration">
          </div>
        </div>
        <hr />
        <div class='creator'>
          <div class='wrapper'>
            <img :src="product.Image || defaultAvatar" width="50" height="50"/>
          <p style="display: inline;"><ins>Creation of</ins> {{ product.ServiceCreater }}</p>
        </div>
        </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const props = defineProps(["productA"]);
const emits = defineEmits(["customCancel"]);
const router = useRouter();

const defaultAvatar = 'https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3396.jpg?w=360';

const defaultImage = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9xah24k1IPo5PF3vksg-gN0503a4yoyuliQ&s';

function goToProfile (userId) {
  localStorage.setItem('userInfoId', userId);
  router.push({ name: 'user-profile', query: { userSessionId: userId } });
};

</script>

<style scoped>

@import '../../assets/shopCard.css';

.imageForSubs0 {
    position: relative;
    width: 32.8%;
    height: 420px;
    margin: 3px;
    padding: 0px;
    overflow: hidden;
    display: flex;
    align-items: center;
    /* 垂直居中 */
    justify-content: center;
    /* 水平居中 */
}

.imageForSubs1 {
    position: relative;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: center;
    overflow: hidden;
}

.imageForSubs2 {
    width: 32.8%;
    height: 80%;
    margin: 3px;
    padding: 0px;
}


.imageForSubs2 img {
    width: 100%;
    object-fit: contain;
    transition: 0.6s opacity;
    opacity: 0.9;
    border: 1px solid #e4dbdb;
}

/* Responsive CSS */
@media (max-width: 1440px) {
    .imageForSubs2 {
        width: 32.8%;
        /* 調整響應式佈局 */
        height: 330px;
        margin: 3px auto;
        /* 確保左右邊界一致 */
    }
}

@media (max-width: 1080px) {
    .imageForSubs2 {
        width: 100%;
        /* 調整響應式佈局 */
        height: 800px;
        margin: 10px auto;
        /* 確保左右邊界一致 */
    }
}

@media (max-width: 600px) {
    .imageForSubs2 {
        width: 100%;
        /* 視窗縮小後顯示1個卡片，並留出左右邊距 */
        height: 450px;
        margin: 10px auto;
        /* 確保左右邊界一致 */
    }
}


.shopCard {
  position: relative;
  width: 300px;
  height: 300px;
  text-align: center;
  background-color: azure;
  overflow: hidden;
  cursor: pointer;
}

img {
  max-width: 300px;
  max-height: 280px;
  object-fit: cover;
}

.shopDescribe {
  display: none;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  align-items: center;
  justify-content: center;
  padding: 10px;
  box-sizing: border-box;
}

.shopCard:hover .shopDescribe {
  display: flex;
}
</style>