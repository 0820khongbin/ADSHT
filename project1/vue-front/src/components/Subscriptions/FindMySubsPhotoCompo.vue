<template>
    <div v-for=" product in productA" :key="product.ServiceId" class="imageForSubs1">   
        <div  v-if=" user.userLastName  == product.ServiceCreater  " v-for="ServPhotoList in product.ServPhotoList" class="imageForSubs2" >
            <div v-for="photo in ServPhotoList">
                    <img :src="photo" :alt="productA.ServiceCreater">
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
const props = defineProps(["productA","user"]);
const emits = defineEmits(["customCancel"]);
</script>

<style scoped>

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
</style>