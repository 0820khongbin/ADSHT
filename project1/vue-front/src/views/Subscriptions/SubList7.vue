<template>
    <h1>你已訂閱項目</h1>
    <div v-for="product in products">
        <FindMySubsCompo :key="product.ServiceId" :productA="product" @customCancel="callCancel">
        </FindMySubsCompo>
    </div>
</template>

<script setup>
import FindMySubsCompo from '@/components/Subscriptions/FindMySubsCompo.vue';
import axiosapi from '@/plugins/axios.js';
import Swal from 'sweetalert2';
import { ref, onMounted } from 'vue';
const products = ref([]);
const showbtn = ref("")

onMounted(function () {
    callFind();
});

function callFind() {
    // let request = {
    //     "start": start.value,
    //     "max": rows.value,
    //     "dir": false,
    //     "order": "price",
    // };
    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    axiosapi.post("/ajax/pages/servicesInfo/findMySubsById").then(function (response) {
        console.log("response:", response);
        products.value = response.data.ServList;
        showbtn.value = "findMySubsById";
    }).catch(function (error) {
        console.log("error", error);
        Swal.fire({
            text: "查詢失敗：" + error.message,
            icon: "error"
        });
    });
}

function callCancel(id) {
    Swal.fire({
        icon: 'question',
        text: '確定要取消訂閱嗎？',
        showCancelButton: true,
        allowOutsideClick: false
    }).then(function (result) {
        if (result.isConfirmed) {
            Swal.fire({
                text: "執行中......",
                allowOutsideClick: false,
                showConfirmButton: false,
            });
            if (id) {
                axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
                axiosapi.delete("/ajax/pages/SubServices/del/" + id).then(function (response) {
                    console.log("response", response);
                    if (response.data.success) {
                        Swal.fire({
                            icon: "success",
                            text: response.data.message,
                        }).then(function (result) {
                            callFind();
                        });
                    } else {
                        Swal.fire({
                            icon: "warning",
                            text: response.data.message,
                        });
                    }
                }).catch(function (error) {
                    console.log("error", error);
                    Swal.fire({
                        icon: "error",
                        text: "刪除錯誤：" + error.message,
                    });
                });
            }
        }
    });
}


</script>

<style scoped></style>