<template>
    <h1>伺服器上全部訂閱項目</h1>
    <div class="container group">
        <FindSubsCompo3 v-for="product in products" :key="product.ServiceId" :productA="product"
            @customDelete="callRemove" @customAdd="callAdd" @customCancel="callCancel">
        </FindSubsCompo3>
    </div>
</template>

<script setup>
import FindSubsCompo3 from '@/components/Subscriptions/FindSubsCompo3.vue'
import axiosapi from '@/plugins/axios.js';
import Swal from 'sweetalert2';
import { ref, onMounted } from 'vue';
const products = ref([]);

onMounted(function () {
    callFind();
});

function callFind(page) {
    // let request = {
    //     "start": start.value,
    //     "max": rows.value,
    //     "dir": false,
    //     "order": "price",
    // };

    axiosapi.post("/ajax/pages/servicesInfo/find", request).then(function (response) {
        console.log("response:", response);
        products.value = response.data.ServList;
    }).catch(function (error) {
        console.log("error", error);
        Swal.fire({
            text: "查詢失敗：" + error.message,
            icon: "error"
        });
    });
}

function callRemove(id) {
    Swal.fire({
        icon: 'question',
        text: '確定要刪除？',
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
                axiosapi.delete("/ajax/pages/servicesInfo/del/" + id).then(function (response) {
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

function callAdd(id) {
    Swal.fire({
        text: "訂閱中......",
        allowOutsideClick: false,
        showConfirmButton: false,
    });
    let request = {
        "servInfoId": id
    };
    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    console.log(["id", id], ["session-id", axiosapi.defaults.headers['session-id']]);
    axiosapi.post("/ajax/pages/SubServices/add", request).then(function (response) {
        console.log("response", response);
        if (response.data.success) {
            Swal.fire({
                icon: "success",
                text: response.data.message,
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
            text: "新增錯誤：" + error.message,
        });
    });
}
</script>

<style scoped></style>