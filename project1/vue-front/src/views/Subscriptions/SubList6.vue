<template>
    <h1>管理建立的訂閱項目</h1>
    <div class="container group">
        <FindSubsCompo2 v-for="product in products" :key="product.ServiceId" :productA="product" :showbtn
            @custom-delete="callRemove" @custom-add="callAdd" @custom-cancel="callCancel" @custom-open="openModal"
            @custom-photo-open="openPhotoModal">
        </FindSubsCompo2>

        <!-- Modal -->
        <ModifySubsCompo2Modal ref="subsModal" v-model="product1" @custom-update="callModify">
        </ModifySubsCompo2Modal>

        <InsertSubsPhotoModal ref="subsPhotoModal" v-model="product1">
        </InsertSubsPhotoModal>

    </div>
</template>

<script setup>
const props = defineProps(["user"]);
import FindSubsCompo2 from '@/components/Subscriptions/FindSubsCompo2.vue'
import ModifySubsCompo2Modal from '@/components/Subscriptions/ModifySubsCompo2Modal.vue';
import InsertSubsPhotoModal from '@/components/Subscriptions/InsertSubsPhotoModal.vue';
import axiosapi from '@/plugins/axios.js';
import Swal from 'sweetalert2';
import { useRoute } from 'vue-router';
// import Paginate from 'vuejs-paginate-next';
import { ref, onMounted } from 'vue';


const products = ref([]);
const product1 = ref({});
const showbtn = ref("")
const subsModal = ref(null);
const subsPhotoModal = ref(null);
const route = useRoute();

onMounted(function () {
    callFind();
});


function openModal(id) {
    callFindById(id);
    subsModal.value.showModal();
}

function openPhotoModal(id) {
    callFindById(id);
    subsPhotoModal.value.showModal();
}

function callFind() {
    // //計算分頁
    // if (page) {
    //   start.value = (page - 1) * rows.value;
    //   current.value = page;
    // } else {
    //   start.value = 0;
    //   current.value = 1;
    // }
    console.log("user：", route.query.id);
    console.log("user：", route.query.name);
    // let request = {
    //     "start": start.value,
    //     "max": rows.value,
    //     "dir": false,
    //     "order": "price",
    // };
    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    axiosapi.post("/ajax/pages/servicesInfo/findMyCreate").then(function (response) {
        console.log("response:", response);
        products.value = response.data.ServList;
        showbtn.value = "findMyCreate";
        // total.value = response.data.count;
        // pages.value = Math.ceil(total.value / rows.value);
        // lastPageRows.value = total.value % rows.value;
        // setTimeout(function() {
        //     Swal.close();
        // }, 500);
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
                            window.location.reload();
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

function callFindById(id) {
    Swal.fire({
        text: "處理中.....",
        allowOutsideClick: false,
        showConfirmButton: false
    });

    axiosapi.get(`/ajax/pages/servicesInfo/findOne/${id}`).then(function (response) {
        product1.value = response.data.list[0];

        setTimeout(function () {
            Swal.close();
        }, 500);
    }).catch(function (error) {
        console.log("error", error);
        Swal.fire({
            text: "查詢失敗：" + error.message,
            icon: "error"
        });
    });
}



function callModify() {
    Swal.fire({
        text: "執行中......",
        allowOutsideClick: false,
        showConfirmButton: false,

    });
    console.log(product1.value);
    let request = {
        "serviceName": product1.value.ServiceName,
        "price": product1.value.Price,
        "description": product1.value.Description,
        "image": product1.value.Image,
        "level": product1.value.ServiceLevel
    };
    //  if (product1.value.ServiceName == "") {
    //      product1.value.ServiceName = null;
    //  }
    //  if (product1.value.Price == "") {
    //      product1.value.Price = null;
    //  }
    //  if (product1.value.Description == "") {
    //      product1.value.Description = null;
    //  }
    //  if (product1.value.Image == "") {
    //      product1.value.Image = null;
    //  }
    axiosapi.put(`/ajax/pages/servicesInfo/update/${product1.value.ServiceId}`, request).then(function (response) {
        console.log("response", response);
        if (response.data.success) {
            Swal.fire({
                icon: "success",
                text: response.data.message,
            }).then(function (result) {
                subsModal.value.hideModal();
                window.location.reload();
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
            text: "修改錯誤：" + error.message,
        });
    });

}

</script>

<style scoped></style>