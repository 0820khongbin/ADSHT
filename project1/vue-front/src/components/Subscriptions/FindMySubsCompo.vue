<template>
    <div class="subscription-list">
        <div class="subscription-item">
            <div class="subscription-header">
                <img :src="productA.ServiceCreaterImg" :alt="productA.ServiceCreater"
                    @click="goToProfile(productA.ServiceCreaterId)">
                <div>
                    <h3>{{ productA.ServiceCreater }}</h3>
                    <h3>訂閱名稱：{{ productA.ServiceName }}</h3>
                    <h3>每月$ {{ productA.Price }}</h3>
                </div>
            </div>
            <button @click="toggleDetails(productA.ServiceId)">顯示訂閱資料</button>
        </div>
        <div v-if="productA.ServiceId === activeSubscriptionId" class="subscription-details">
            <p>你好：</p>
            <p>感謝你成為這個朋友的會員</p>
            <h3 v-if="productA.ServiceLevel">會員等級：{{ productA.ServiceLevel }}</h3>
            <span>會員勳章：<img :src="productA.Image" width="50" height="50"></span>
            <h3>會員的內容: {{ productA.Description }}</h3>
            <a href="#" @click="emits('customCancel', productA.ServiceId)"><font-awesome-icon :icon="['fas', 'xmark']"
                    size="2xl" />取消訂閱</a>
            <div class="subscription-actions">

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
const activeSubscriptionId = ref(null);

function toggleDetails(id) {
    activeSubscriptionId.value = activeSubscriptionId.value === id ? null : id;
}

function goToProfile(userId) {
    console.log("user", userId)
    localStorage.setItem('userInfoId', userId);
    router.push({ name: 'user-profile', query: { userSessionId: userId } });
};
</script>

<style scoped>
.subscription-list {
    background-color: rgba(0, 0, 0, 0.6);
    color: rgb(246, 245, 245);
    padding: 0px;
    width: 800px;
    margin: 0 auto;
}

.subscription-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border: 1px solid #1a1dd4;
    padding: 10px;
    margin-bottom: 10px;
}

.subscription-header {
    display: flex;
    align-items: center;
}

.subscription-header img {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    margin-right: 10px;
}

button {
    background-color: #333;
    color: white;
    border: none;
    padding: 5px 10px;
    cursor: pointer;
}

button:hover {
    background-color: #555;
}

.subscription-details {
    padding: 10px;
    margin-top: 10px;
    /* border-top: 1px solid #333; */
    padding-top: 10px;
    margin-bottom: 10px;
}

.subscription-actions {
    display: flex;
    justify-content: space-between;
}

.subscription-actions a {
    color: #1e90ff;
    text-decoration: none;
    margin-right: 10px;
}

.subscription-actions a:hover {
    text-decoration: underline;
}
</style>